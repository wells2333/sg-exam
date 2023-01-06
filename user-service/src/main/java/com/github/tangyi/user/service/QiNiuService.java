package com.github.tangyi.user.service;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.oss.utils.QiNiuUtil;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.common.utils.image.RandomImageUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.constants.AttachConstant;
import com.github.tangyi.user.enums.AttachTypeEnum;
import com.qiniu.common.QiniuException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class QiNiuService {

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	private final SysProperties sysProperties;

	@Transactional
	public Attachment upload(MultipartFile file, String groupCode) throws IOException {
		return upload(file, groupCode, SysUtil.getUser(), SysUtil.getTenantCode());
	}

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
		// groupCode作为目录
		String fileName = getName(attachment.getGroupCode(), attachment.getAttachName());
		String result = QiNiuUtil.getInstance().upload(bytes, fileName);
		attachment.setUploadResult(result);
		// 生成默认的访问链接
		attachment.setUrl(getDownloadUrl(fileName, AttachConstant.DEFAULT_EXPIRE));
		save(attachment);
	}

	@Transactional
	public int save(Attachment attachment) {
		return attachmentService.insert(attachment);
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public boolean delete(Attachment attachment) throws QiniuException {
		SgPreconditions.checkNull(attachment, "attachment is null");
		if (attachmentService.delete(attachment) > 0 && attachment.getGroupCode() != null) {
			AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
			if (group != null) {
				return QiNiuUtil.getInstance().delete(getName(group.getGroupCode(), attachment.getAttachName()));
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

	public String getDownloadUrl(AttachGroup group, String attachName) {
		return getDownloadUrl(getName(group.getGroupCode(), attachName), group.getUrlExpire());
	}

	public String getDownloadUrl(String fileName, int expire) {
		if (expire <= 0) {
			expire = AttachConstant.DEFAULT_EXPIRE;
		}
		return QiNiuUtil.getInstance().getDownloadUrl(fileName, expire);
	}

	public boolean isNotDefaultGroup(Attachment attachment) {
		return !Group.DEFAULT.equals(attachment.getGroupCode());
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
				String url = getDownloadUrl(getName(group.getGroupCode(), attachment.getAttachName()),
						group.getUrlExpire());
				attachment.setUrl(url);
			}
		}
		return attachment;
	}

	@Transactional
	public Long createRandomImage(String groupCode) {
		String name = RandomImageUtil.randomImage(sysProperties.getDefaultImageCount(),
				sysProperties.getDefaultImageType());
		String url = getDownloadUrl(getName(AttachTypeEnum.DEFAULT_IMAGE.getValue(), name),
				AttachConstant.DEFAULT_EXPIRE);
		Attachment attachment = new Attachment();
		attachment.setCommonValue();
		attachment.setUrl(url);
		attachment.setAttachType(FileUtil.getFileNameEx(url));
		attachment.setAttachName(FileUtil.getFileNameFromUrl(url));
		attachment.setGroupCode(groupCode);
		attachmentService.insert(attachment);
		log.info("random image url, groupCode: {}, url: {}, id: {}", groupCode, attachment.getUrl(),
				attachment.getId());
		return attachment.getId();
	}

	public String getName(String groupCode, String fileName) {
		if (groupCode != null) {
			return groupCode + "/" + fileName;
		}
		return fileName;
	}
}
