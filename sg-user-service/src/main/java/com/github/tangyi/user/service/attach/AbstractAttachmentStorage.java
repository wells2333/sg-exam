/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user.service.attach;

import cn.hutool.crypto.SecureUtil;
import com.github.tangyi.api.user.attach.AttachmentStorage;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.HashUtil;
import com.github.tangyi.common.utils.SysUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.thread.ExecutorHolder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class AbstractAttachmentStorage implements AttachmentStorage {

	private static final int ATTACHMENT_STORAGE_SHARD_SIZE = EnvUtils.getInt("ATTACHMENT_STORAGE_SHARD_SIZE", 256);

	protected static final String PART_ETG = "etag";
	protected static final String PART_NUMBER = "partNumber";

	protected final AttachmentService attachmentService;
	protected final AttachGroupService groupService;
	protected final DefaultImageService defaultImageService;
	protected final ExecutorHolder executorHolder;

	public AbstractAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
			DefaultImageService defaultImageService, ExecutorHolder executorHolder) {
		this.attachmentService = attachmentService;
		this.groupService = groupService;
		this.defaultImageService = defaultImageService;
		this.executorHolder = executorHolder;
	}

	protected String preUpload(Attachment attachment) {
		SgPreconditions.checkNull(attachment, "attachment is null");
		SgPreconditions.checkNull(attachment.getGroupCode(), "groupCode must not null");
		// groupCode 作为目录
		return getShardName(attachment.getGroupCode(), attachment.getAttachName(), attachment.getHash());
	}

	protected String getShardName(String groupCode, String fileName, String hash) {
		return this.getShardName(groupCode, fileName, hash, true);
	}

	/**
	 * 计算文件的完成路径
	 * @param groupCode 文件分组
	 * @param fileName 文件名
	 * @param hash 文件的哈希值
	 * @param md5WithNanoTime 计算 md5 时是否加入当期时间（nanoTime）
	 */
	protected String getShardName(String groupCode, String fileName, String hash, boolean md5WithNanoTime) {
		// 基于 md5 重新命名文件名
		String md5Params = md5WithNanoTime ? System.nanoTime() + fileName + hash : fileName + hash;
		String md5 = SecureUtil.md5(md5Params);
		String newFilename = md5 + "." + FilenameUtils.getExtension(fileName);
		if (groupCode != null) {
			String id = StringUtils.isNotBlank(hash) ? hash : newFilename;
			int shardId = HashUtil.getShardId(id, ATTACHMENT_STORAGE_SHARD_SIZE);
			newFilename = getName(groupCode, shardId, newFilename);
		}
		log.info("Get shard name finished, fileName: {}, newFilename: {}", fileName, newFilename);
		return newFilename;
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

	public abstract void doDelete(String fileName) throws IOException;

	@Override
	public Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode, String hash) {
		SgPreconditions.checkNull(groupCode, "groupCode is null");
		Attachment attachment = new Attachment();
		attachment.setCommonValue(user, tenantCode);
		attachment.setAttachType(FileUtil.getFileNameEx(fileName));
		if (bytes != null) {
			attachment.setAttachSize(String.valueOf(bytes.length));
		}
		attachment.setAttachName(originalFilename);
		attachment.setGroupCode(groupCode);
		attachment.setHash(hash);
		return attachment;
	}

	@Transactional
	@CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
	public boolean delete(Attachment attachment) throws IOException {
		SgPreconditions.checkNull(attachment, "attachment is null");
		if (attachmentService.delete(attachment) > 0 && attachment.getGroupCode() != null) {
			AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
			if (group != null) {
				String fileName = getShardName(group.getGroupCode(), attachment.getAttachName(), attachment.getHash());
				log.info("Deleting file {} ...", fileName);
				doDelete(fileName);
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

	public String getDownloadUrl(AttachGroup group, String attachName, String hash) {
		String fileName = getShardName(group.getGroupCode(), attachName, hash);
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
				String fileName = getShardName(group.getGroupCode(), attachment.getAttachName(), attachment.getHash());
				String url = getDownloadUrl(fileName, group.getUrlExpire());
				attachment.setUrl(url);
			}
		}
		return attachment;
	}

	@Override
	@Transactional
	public Long defaultImage(String groupCode) {
		BytesUploadContext context = new BytesUploadContext();
		context.setGroup(groupService.findByGroupCode(groupCode));
		// ${nanoTime}.jpeg
		context.setFileName(System.nanoTime() + DefaultImageService.DEFAULT_IMAGE_SUFFIX);
		context.setBytes(defaultImageService.randomImage());
		context.setOriginalFilename(context.getFileName());
		context.setUser(SysUtil.getUser());
		context.setTenantCode(SysUtil.getTenantCode());
		Attachment res = this.upload(context);
		String url = res.getUrl();
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

	@Data
	@ToString
	@Accessors(chain = true)
	static class ChunkUploadContext {

		private File targetFile;
		private String key;
		private String contentType;
		private int uploadChunkSizeMb;
	}
}
