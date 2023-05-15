package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.FileUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.*;
import com.github.tangyi.user.constants.AttachConstant;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QiNiuAttachmentStorage extends AbstractAttachmentStorage {

	private static final int QI_NIU_UPLOAD_PART_SIZE_MB = EnvUtils.getInt("QI_NIU_UPLOAD_PART_SIZE_MB", 10);

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	private final SysProperties props;

	private final QiNiuConfig qiNiuConfig;

	private final ExecutorHolder executorHolder;

	public QiNiuAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
			AttachmentService attachmentService1, AttachGroupService groupService1, SysProperties props,
			QiNiuConfig qiNiuConfig, ExecutorHolder executorHolder) {
		super(attachmentService, groupService);
		this.attachmentService = attachmentService1;
		this.groupService = groupService1;
		this.props = props;
		this.qiNiuConfig = qiNiuConfig;
		this.executorHolder = executorHolder;
	}

	@Override
	@Transactional
	public Attachment upload(MultipartFileUploadContext context) throws IOException {
		String groupCode = context.getGroup().getGroupCode();
		MultipartFile file = context.getMultipartFile();
		return this.upload(groupCode, file.getOriginalFilename(), file.getOriginalFilename(), file.getBytes(),
				context.getUser(), context.getTenantCode());
	}

	@Override
	@Transactional
	public Attachment upload(FileUploadContext context) throws IOException, ExecutionException, InterruptedException {
		StopWatch watch = StopWatchUtil.start();
		String groupCode = context.getGroup().getGroupCode();
		File targetFile = context.getTargetFile();
		Attachment attachment = prepareAttachment(groupCode, targetFile.getName(), targetFile.getName(), null,
				context.getUser(), context.getTenantCode());
		try {
			String key = preUpload(attachment);
			this.multiPartUpload(targetFile, key, context.isDeleteTargetFileAfterUploaded());
			doAfterUpload(attachment, key, key, watch);
		} catch (QiniuException ex) {
			throw new OssException(ex, "Failed to upload file");
		}
		return attachment;
	}

	@Override
	@Transactional
	public Attachment upload(BytesUploadContext context) {
		String groupCode = context.getGroup().getGroupCode();
		return this.upload(groupCode, context.getFileName(), context.getOriginalFilename(), context.getBytes(),
				context.getUser(), context.getTenantCode());
	}

	@Transactional
	public Attachment upload(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode) {
		Attachment attachment = prepareAttachment(groupCode, fileName, originalFilename, bytes, user, tenantCode);
		upload(attachment, bytes);
		return attachment;
	}

	@Transactional
	public void upload(Attachment attachment, byte[] bytes) {
		String fileName = preUpload(attachment);
		StopWatch watch = StopWatchUtil.start();
		try {
			Response response = getUploadManager().put(bytes, fileName, getQiNiuToken());
			DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
			doAfterUpload(attachment, putRet.key, fileName, watch);
		} catch (QiniuException ex) {
			throw new OssException(ex, "Failed to upload file");
		}
	}

	@Transactional
	public void doAfterUpload(Attachment attachment, String key, String fileName, StopWatch watch) {
		String took = StopWatchUtil.stop(watch);
		log.info("Upload file done successfully, fileName: {}, took: {}", fileName, took);
		String result = qiNiuConfig.getDomainOfBucket() + "/" + key;
		attachment.setUploadResult(result);
		attachment.setUrl(getDownloadUrl(fileName, AttachConstant.DEFAULT_EXPIRE));
		if (attachment.getId() == null) {
			attachmentService.insert(attachment);
		} else {
			attachmentService.update(attachment);
		}
	}

	public void multiPartUpload(File targetFile, String key, boolean deleteTargetFileAfterUploaded)
			throws IOException, ExecutionException, InterruptedException {
		long start = System.nanoTime();
		ApiUploadV2CompleteUpload.Response completeRes = doMultiPartUpload(targetFile, key);
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		log.info("The multi part file upload finished, fileName: {}, key: {}, took: {}ms, response: {}",
				targetFile.getName(), key, took, completeRes.getResponse());
		if (deleteTargetFileAfterUploaded && targetFile.delete()) {
			log.info("The original file has been deleted, fileName: {}", targetFile.getName());
		}
	}

	private ApiUploadV2CompleteUpload.Response doMultiPartUpload(File targetFile, String key)
			throws IOException, ExecutionException, InterruptedException {
		String fileName = targetFile.getName();
		String token = getQiNiuToken();
		Client client = getClient();
		String urlPrefix = getUrlPrefix(token, client);
		// 初始化
		String uploadId = initUploadId(urlPrefix, token, client, key);
		// 上传文件分片
		List<Map<String, Object>> partsInfo = uploadParts(targetFile, urlPrefix, token, client, key, uploadId);
		// 合并文件分片
		Map<String, Object> param = Maps.newHashMap();
		ApiUploadV2CompleteUpload api = new ApiUploadV2CompleteUpload(client);
		ApiUploadV2CompleteUpload.Request req = new ApiUploadV2CompleteUpload.Request(urlPrefix, token, uploadId,
				partsInfo).setKey(key).setFileName(fileName).setCustomParam(param);
		return api.request(req);
	}

	private String getUrlPrefix(String token, Client client) throws QiniuException {
		ApiQueryRegion api = new ApiQueryRegion(client);
		ApiQueryRegion.Response res = api.request(new ApiQueryRegion.Request(null, token));
		List<String> hosts = res.getDefaultRegionUpHosts();
		if (CollectionUtils.isEmpty(hosts)) {
			throw new CommonException("Failed to query region up hosts");
		}
		String host = hosts.get(0);
		if (!host.startsWith("http")) {
			host = "http://" + host;
		}
		return host;
	}

	/**
	 * 初始化上传，返回 uploadId
	 */
	private String initUploadId(String urlPrefix, String token, Client client, String key) throws QiniuException {
		ApiUploadV2InitUpload api = new ApiUploadV2InitUpload(client);
		ApiUploadV2InitUpload.Request req = new ApiUploadV2InitUpload.Request(urlPrefix, token).setKey(key);
		ApiUploadV2InitUpload.Response res = api.request(req);
		return res.getUploadId();
	}

	/**
	 * 多线程上传文件分片
	 */
	private List<Map<String, Object>> uploadParts(File targetFile, String urlPrefix, String token, Client client,
			String key, String uploadId) throws IOException, ExecutionException, InterruptedException {
		List<Map<String, Object>> partsInfo = Lists.newArrayList();
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(targetFile, "r")) {
			ListeningExecutorService executor = executorHolder.getQiNiuUploadExecutor();
			List<Future<Map<String, Object>>> futures = Lists.newArrayList();
			long fileSize = targetFile.length();
			int defaultPartSize = 1024 * 1024 * QI_NIU_UPLOAD_PART_SIZE_MB;
			long partOffset = 0;
			int partNumber = 1;
			while (partOffset < fileSize) {
				long partSize = fileSize - partOffset;
				if (partSize > defaultPartSize) {
					partSize = defaultPartSize;
				}
				final long finalPartOffset = partOffset;
				final long finalPartSize = partSize;
				final int finalPartNumber = partNumber;
				futures.add(executor.submit(
						() -> doUploadPart(randomAccessFile, finalPartOffset, finalPartSize, urlPrefix, token, client,
								key, uploadId, finalPartNumber)));
				partNumber += 1;
				partOffset += partSize;
			}
			for (Future<Map<String, Object>> future : futures) {
				Map<String, Object> partInfo = future.get();
				if (partInfo != null) {
					partsInfo.add(partInfo);
				}
			}
		}
		return partsInfo;
	}

	private Map<String, Object> doUploadPart(RandomAccessFile randomAccessFile, long partOffset, long partSize,
			String urlPrefix, String token, Client client, String key, String uploadId, int partNumber)
			throws QiniuException {
		long start = System.nanoTime();
		byte[] partData = getUploadData(randomAccessFile, partOffset, (int) partSize);
		if (partData == null) {
			throw new CommonException("Failed to get upload part data");
		}
		ApiUploadV2UploadPart api = new ApiUploadV2UploadPart(client);
		ApiUploadV2UploadPart.Request req = new ApiUploadV2UploadPart.Request(urlPrefix, token, uploadId,
				partNumber).setKey(key).setUploadData(partData, 0, partData.length, null);
		ApiUploadV2UploadPart.Response res = api.request(req);
		String etag = res.getEtag();
		Map<String, Object> partInfo = Maps.newHashMap();
		partInfo.put(ApiUploadV2CompleteUpload.Request.PART_NUMBER, partNumber);
		partInfo.put(ApiUploadV2CompleteUpload.Request.PART_ETG, etag);
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		log.info("Upload part finished, key: {}, partNumber: {}, uploadId: {}, took: {}ms, response: {}", key,
				partNumber, uploadId, took, res.getResponse());
		return partInfo;
	}

	private byte[] getUploadData(RandomAccessFile file, long offset, int size) {
		byte[] uploadData = new byte[size];
		try {
			file.seek(offset);
			int readSize = 0;
			while (readSize != size) {
				int s = 0;
				try {
					s = file.read(uploadData, readSize, size - readSize);
				} catch (IOException e) {
					log.error("Failed to get upload data", e);
				}
				if (s >= 0) {
					readSize += s;
				} else {
					break;
				}
			}
		} catch (IOException e) {
			uploadData = null;
		}
		return uploadData;
	}

	@Override
	public void doDelete(Attachment attachment, String fileName) throws IOException {
		try {
			BucketManager manager = getBucketManager(getAuth(), new Configuration(Region.region2()));
			manager.delete(qiNiuConfig.getBucket(), fileName);
		} catch (QiniuException e) {
			if (StringUtils.contains(e.getMessage(), "no such file or directory")) {
				log.warn("Delete attachment failed: no such file or directory, fileName: {}", fileName);
			} else {
				throw e;
			}
		}
	}

	@Transactional
	public Long randomAttachmentId(String groupCode) {
		String name = RandomImageUtil.randomImage(props.getDefaultImageCount(), props.getDefaultImageType());
		String fileName = getName(AttachTypeEnum.DEFAULT_IMAGE.getValue(), name);
		String url = getDownloadUrl(fileName, AttachConstant.DEFAULT_EXPIRE);
		Attachment attachment = new Attachment();
		attachment.setCommonValue();
		attachment.setUrl(url);
		attachment.setAttachType(FileUtil.getFileNameEx(url));
		attachment.setAttachName(FileUtil.getFileNameFromUrl(url));
		attachment.setGroupCode(groupCode);
		attachmentService.insert(attachment);
		log.info("Generate random image successfully, groupCode: {}, url: {}, id: {}", groupCode, attachment.getUrl(),
				attachment.getId());
		return attachment.getId();
	}

	public String getDownloadUrl(String fileName, long expire) {
		if (expire <= 0) {
			expire = AttachConstant.DEFAULT_EXPIRE;
		}
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
		String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
		return getAuth().privateDownloadUrl(publicUrl, expire);
	}

	private Auth getAuth() {
		if (StringUtils.isNotBlank(qiNiuConfig.getAccessKey()) && StringUtils.isNotBlank(qiNiuConfig.getSecretKey())) {
			return Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
		}
		throw new IllegalArgumentException("QiNiu Auth is null, please check your qiNiuConfig");
	}

	private UploadManager getUploadManager() {
		Configuration config = new Configuration(Region.autoRegion());
		return new UploadManager(config);
	}

	private BucketManager getBucketManager(Auth auth, Configuration config) {
		return new BucketManager(auth, config);
	}

	private String getQiNiuToken() {
		return getAuth().uploadToken(qiNiuConfig.getBucket());
	}

	private Client getClient() {
		Configuration configuration = new Configuration(Region.autoRegion());
		return new Client(configuration);
	}
}
