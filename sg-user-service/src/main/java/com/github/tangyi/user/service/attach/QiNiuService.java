package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.service.IQiNiuService;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.*;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QiNiuService implements IQiNiuService {

	private static final int QINIU_SHARD_SIZE = EnvUtils.getInt("QINIU_SHARD_SIZE", 256);

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	private final SysProperties props;

	private final QiNiuConfig qiNiuConfig;

	@Transactional
	public Attachment upload(MultipartFile file, String groupCode, String user, String tenantCode) throws IOException {
		return upload(groupCode, file.getOriginalFilename(), file.getOriginalFilename(), file.getBytes(), user,
				tenantCode);
	}

	@Transactional
	public Attachment upload(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode) {
		SgPreconditions.checkNull(groupCode, "group code is null");
		SgPreconditions.checkNull(bytes, "bytes is null");
		Attachment attachment = new Attachment();
		attachment.setCommonValue(user, tenantCode);
		attachment.setAttachType(FileUtil.getFileNameEx(fileName));
		attachment.setAttachSize(String.valueOf(bytes.length));
		attachment.setAttachName(originalFilename);
		attachment.setGroupCode(groupCode);
		upload(attachment, bytes);
		return attachment;
	}

	@Transactional
	public void upload(Attachment attachment, byte[] bytes) {
		SgPreconditions.checkNull(attachment, "attachment is null");
		SgPreconditions.checkNull(bytes, "attachment bytes is null");
		SgPreconditions.checkNull(attachment.getGroupCode(), "group code must not null");
		// groupCode 作为目录
		String fileName = getShardName(attachment.getGroupCode(), attachment.getAttachName());
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

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public boolean delete(Attachment attachment) throws QiniuException {
		SgPreconditions.checkNull(attachment, "attachment is null");
		if (attachmentService.delete(attachment) > 0 && attachment.getGroupCode() != null) {
			AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
			if (group != null) {
				String fileName = getShardName(group.getGroupCode(), attachment.getAttachName());
				log.info("Deleting file {} ...", fileName);
				BucketManager manager = getBucketManager(getAuth(), new Configuration(Region.region2()));
				manager.delete(qiNiuConfig.getBucket(), fileName);
				log.info("File has been deleted, fileName: {}", fileName);
				return true;
			}
		}
		return false;
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, allEntries = true)
	public boolean deleteAll(List<Attachment> attachments) throws QiniuException {
		boolean result = false;
		for (Attachment attachment : attachments) {
			if (isNotDefaultGroup(attachment)) {
				result = this.delete(attachment);
			}
		}
		return result;
	}

	public boolean delete(String fileName) throws QiniuException {
		log.info("Deleting file {} ...", fileName);
		getBucketManager(getAuth(), new Configuration(Region.region2())).delete(qiNiuConfig.getBucket(), fileName);
		log.info("File has been deleted, fileName: {}", fileName);
		return Boolean.TRUE;
	}

	public String getDownloadUrl(AttachGroup group, String attachName) {
		String fileName = getShardName(group.getGroupCode(), attachName);
		return getDownloadUrl(fileName, group.getUrlExpire());
	}

	public String getDownloadUrl(String fileName, long expire) {
		if (expire <= 0) {
			expire = AttachConstant.DEFAULT_EXPIRE;
		}
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
		String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
		return getAuth().privateDownloadUrl(publicUrl, expire);
	}

	@Cacheable(value = UserCacheName.ATTACHMENT_URL, key = "#id", unless = "#result == null")
	public String getPreviewUrl(Long id) {
		Attachment attachment = getPreviewAttachment(id);
		return attachment != null ? attachment.getUrl() : null;
	}

	public Attachment getPreviewAttachment(Long id) {
		Attachment attachment = attachmentService.get(id);
		if (attachment != null && StringUtils.isEmpty(attachment.getUrl())) {
			AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
			if (group != null) {
				String fileName = getShardName(group.getGroupCode(), attachment.getAttachName());
				String url = getDownloadUrl(fileName, group.getUrlExpire());
				attachment.setUrl(url);
			}
		}
		return attachment;
	}

	@Transactional
	public Long randomImage(String groupCode) {
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

	private String getShardName(String groupCode, String fileName) {
		if (groupCode != null) {
			int shardId = HashUtil.getShardId(fileName, QINIU_SHARD_SIZE);
			return getName(groupCode, shardId, fileName);
		}
		return fileName;
	}

	private String getName(String groupCode, String fileName) {
		return getName(groupCode, -1, fileName);
	}

	private String getName(String groupCode, int shardId, String fileName) {
		String res = groupCode + "/";
		if (shardId > 0) {
			res = res + shardId + "/";
		}
		return res + fileName;
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

	private boolean isNotDefaultGroup(Attachment attachment) {
		return !Group.DEFAULT.equals(attachment.getGroupCode());
	}
}
