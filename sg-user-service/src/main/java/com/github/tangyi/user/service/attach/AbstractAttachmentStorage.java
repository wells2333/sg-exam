package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.AttachmentStorage;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.HashUtil;
import com.github.tangyi.constants.UserCacheName;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractAttachmentStorage implements AttachmentStorage {

	private static final int ATTACHMENT_STORAGE_SHARD_SIZE = EnvUtils.getInt("ATTACHMENT_STORAGE_SHARD_SIZE", 256);

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	public AbstractAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService) {
		this.attachmentService = attachmentService;
		this.groupService = groupService;
	}

	protected Attachment prepareAttachment(String groupCode, String fileName, String originalFilename, byte[] bytes,
			String user, String tenantCode) {
		SgPreconditions.checkNull(groupCode, "groupCode is null");
		SgPreconditions.checkNull(bytes, "bytes is null");
		Attachment attachment = new Attachment();
		attachment.setCommonValue(user, tenantCode);
		attachment.setAttachType(FileUtil.getFileNameEx(fileName));
		attachment.setAttachSize(String.valueOf(bytes.length));
		attachment.setAttachName(originalFilename);
		attachment.setGroupCode(groupCode);
		return attachment;
	}

	protected String preUpload(Attachment attachment, byte[] bytes) {
		SgPreconditions.checkNull(attachment, "attachment is null");
		SgPreconditions.checkNull(bytes, "attachment bytes is null");
		SgPreconditions.checkNull(attachment.getGroupCode(), "groupCode must not null");
		// groupCode 作为目录
		return getShardName(attachment.getGroupCode(), attachment.getAttachName());
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public boolean delete(Attachment attachment) throws IOException {
		SgPreconditions.checkNull(attachment, "attachment is null");
		if (attachmentService.delete(attachment) > 0 && attachment.getGroupCode() != null) {
			AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
			if (group != null) {
				String fileName = getShardName(group.getGroupCode(), attachment.getAttachName());
				log.info("Deleting file {} ...", fileName);
				doDelete(attachment, fileName);
				log.info("File has been deleted, fileName: {}", fileName);
				return true;
			}
		}
		return false;
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, allEntries = true)
	public boolean deleteAll(List<Attachment> attachments) throws IOException {
		boolean result = false;
		for (Attachment attachment : attachments) {
			if (isNotDefaultGroup(attachment)) {
				result = this.delete(attachment);
			}
		}
		return result;
	}

	public String getDownloadUrl(AttachGroup group, String attachName) {
		String fileName = getShardName(group.getGroupCode(), attachName);
		return getDownloadUrl(fileName, group.getUrlExpire());
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

	public abstract void doDelete(Attachment attachment, String fileName) throws IOException;

	protected String getShardName(String groupCode, String fileName) {
		if (groupCode != null) {
			int shardId = HashUtil.getShardId(fileName, ATTACHMENT_STORAGE_SHARD_SIZE);
			return getName(groupCode, shardId, fileName);
		}
		return fileName;
	}

	protected String getName(String groupCode, String fileName) {
		return getName(groupCode, -1, fileName);
	}

	protected String getName(String groupCode, int shardId, String fileName) {
		String res = groupCode + "/";
		if (shardId > 0) {
			res = res + shardId + "/";
		}
		return res + fileName;
	}

	private boolean isNotDefaultGroup(Attachment attachment) {
		return !Group.DEFAULT.equals(attachment.getGroupCode());
	}
}
