package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.common.utils.RandomImageUtil;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.constants.AttachConstant;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class QiNiuAttachmentStorage extends AbstractAttachmentStorage {

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	private final SysProperties props;

	private final QiNiuConfig qiNiuConfig;

	public QiNiuAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
			AttachmentService attachmentService1, AttachGroupService groupService1, SysProperties props,
			QiNiuConfig qiNiuConfig) {
		super(attachmentService, groupService);
		this.attachmentService = attachmentService1;
		this.groupService = groupService1;
		this.props = props;
		this.qiNiuConfig = qiNiuConfig;
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
		String fileName = preUpload(attachment, bytes);
		StopWatch watch = StopWatchUtil.start();
		try {
			Response response = getUploadManager().put(bytes, fileName, getQiNiuToken());
			DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
			String took = StopWatchUtil.stop(watch);
			log.info("Upload file done successfully, fileName: {}, took: {}", fileName, took);
			String result = qiNiuConfig.getDomainOfBucket() + "/" + putRet.key;
			attachment.setUploadResult(result);
			attachment.setUrl(getDownloadUrl(fileName, AttachConstant.DEFAULT_EXPIRE));
			attachmentService.insert(attachment);
		} catch (QiniuException ex) {
			log.error("Failed to upload file", ex);
			throw new OssException(ex, "Failed to upload file");
		}
	}

	@Override
	public void doDelete(Attachment attachment, String fileName) throws IOException {
		BucketManager manager = getBucketManager(getAuth(), new Configuration(Region.region2()));
		manager.delete(qiNiuConfig.getBucket(), fileName);
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
}
