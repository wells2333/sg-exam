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

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.user.attach.*;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.exceptions.AttachNotExistException;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.utils.SysUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AttachmentManagerImpl implements AttachmentManager {

	private final Map<Integer, AttachmentStorage> storageMap = Maps.newHashMap();
	private final AttachmentService attachmentService;
	private final AttachGroupService groupService;
	private final QiNiuAttachmentStorage qiNiuStorage;
	private final SysAttachmentChunkService attachmentChunkService;

	public AttachmentManagerImpl(AttachmentService attachmentService, AttachGroupService groupService,
			QiNiuAttachmentStorage qiNiuStorage, MinioAttachmentStorage minioAttachmentStorage,
			SysAttachmentChunkService attachmentChunkService) {
		this.attachmentService = attachmentService;
		this.groupService = groupService;
		this.qiNiuStorage = qiNiuStorage;
		this.attachmentChunkService = attachmentChunkService;
		this.register(AttachmentConstant.QI_NIU, qiNiuStorage);
		this.register(AttachmentConstant.MINIO, minioAttachmentStorage);
	}

	private void register(Integer storageType, AttachmentStorage storage) {
		SgPreconditions.checkNull(storageType, "storageType must not be null");
		SgPreconditions.checkNull(storage, "storage must not be null");
		storageMap.put(storageType, storage);
	}

	private AttachmentStorage getManager(AttachGroup group) {
		SgPreconditions.checkNull(group, "AttachGroup must not be null");
		AttachmentStorage storage = this.storageMap.get(group.getStorageType());
		if (storage == null) {
			log.warn("storage not found, storageType: {}, use qiNiuStorage", group.getStorageType());
			return qiNiuStorage;
		}
		return storage;
	}

	private AttachGroup getAttachGroupByAttachmentId(Long id) {
		Attachment attachment = attachmentService.getNotNullAttachment(id);
		return groupService.findByGroupCode(attachment.getGroupCode());
	}

	@Override
	public Attachment upload(MultipartFileUploadContext context) throws IOException {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment upload(BytesUploadContext context) {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment prepareUploadChunks(AttachGroup group, Attachment attachment) {
		String hash = attachment.getHash();
		SgPreconditions.checkBlank(hash, "hash must not be null");
		String tenantCode = SysUtil.getTenantCode();
		Attachment exists = attachmentService.findByHash(hash, tenantCode);
		if (exists != null) {
			attachmentService.delete(exists);
			log.info("Delete exists attachment: {}", JSON.toJSONString(exists));
		}
		AttachmentStorage storage = getManager(group);
		String user = SysUtil.getUser();
		Attachment prepare = storage.prepare(group.getGroupCode(), attachment.getAttachName(),
				attachment.getAttachName(), null, user, tenantCode, hash);
		prepare.setHash(attachment.getHash());
		prepare.setAttachSize(attachment.getAttachSize());
		int update = attachmentService.insert(prepare);
		int chunkDeleteRes = attachmentChunkService.deleteByHash(hash, tenantCode);
		log.info("Prepare upload chunks finished, hash: {}, id: {}, update: {}, chunkDeleteRes: {}", hash,
				prepare.getId(), update, chunkDeleteRes);
		return prepare;
	}

	@Override
	public Boolean uploadChunk(ChunkUploadContext context) throws IOException {
		MultipartFile multipartFile = context.getMultipartFile();
		String hash = context.getHash();
		Integer index = context.getIndex();
		SgPreconditions.checkNull(multipartFile, "Chunk file must not be null");
		SgPreconditions.checkBlank(hash, "Chunk file hash must not be null");
		SgPreconditions.checkNull(index, "Chunk file index must not be null");
		SgPreconditions.checkNull(context.getUploadId(), "Chunk uploadId must not be null");
		Attachment prepare = attachmentService.findByHash(hash, SysUtil.getTenantCode());
		SgPreconditions.checkNull(prepare, "Chunks are not prepare");
		String filename = prepare.getAttachName();
		AttachGroup group = groupService.findByGroupCode(prepare.getGroupCode());
		context.setGroup(group);
		context.setFilename(filename);
		String chunkName = getManager(group).uploadChunk(context);
		SgPreconditions.checkNull(chunkName, "Failed to upload chunk.");
		log.info("Upload chunk file finished, hash: {}, filename: {}, index: {}", hash, filename, index);
		SysAttachmentChunk chunk = new SysAttachmentChunk();
		chunk.setCommonValue();
		chunk.setChunkName(chunkName);
		chunk.setChunkNumber(index);
		chunk.setChunkDataSize((int) multipartFile.getSize());
		chunk.setChunkStatus(1);
		chunk.setChunkUploadRes(chunkName);
		chunk.setHash(hash);
		return attachmentChunkService.insert(chunk) > 0;
	}

	@Override
	@Transactional
	public Attachment mergeChunks(String hash) throws CommonException {
		String tenantCode = SysUtil.getTenantCode();
		Attachment prepare = attachmentService.findByHash(hash, tenantCode);
		SgPreconditions.checkNull(prepare, "Chunks are not prepare");
		List<SysAttachmentChunk> chunks = attachmentChunkService.findByHash(hash, tenantCode);
		SgPreconditions.checkNull(chunks, "Chunks not found");
		AttachGroup group = groupService.findByGroupCode(prepare.getGroupCode());
		Attachment attachment = getManager(group).mergeChunks(prepare, group, chunks);
		if (attachment != null) {
			attachmentService.update(attachment);
		}
		return attachment;
	}

	@Override
	public boolean delete(Attachment attachment) throws IOException {
		AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
		return getManager(group).delete(attachment);
	}

	@Override
	public boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException {
		return getManager(group).deleteAll(attachments);
	}

	@Override
	public String getDownloadUrl(AttachGroup group, String attachName, String hash) {
		return getManager(group).getDownloadUrl(group, attachName, hash);
	}

	@Override
	public String getPreviewUrl(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewUrl(id);
	}

	@Override
	public String getPreviewUrlIgnoreException(Long id) {
		try {
			AttachGroup group = getAttachGroupByAttachmentId(id);
			return getManager(group).getPreviewUrl(id);
		} catch (AttachNotExistException e) {
			log.warn(e.getMessage());
		}
		return null;

	}

	@Override
	public Attachment getPreviewAttachment(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewAttachment(id);
	}

	@Override
	public Long defaultImage(String groupCode) {
		AttachmentStorage storage = this.storageMap.get(AttachTypeEnum.DEFAULT.getDefaultStorageType());
		AttachGroup group = groupService.findByGroupCode(groupCode);
		return storage.defaultImage(group.getGroupCode());
	}
}
