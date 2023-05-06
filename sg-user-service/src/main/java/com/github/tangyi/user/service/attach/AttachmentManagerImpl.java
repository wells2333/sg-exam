package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.AttachmentManager;
import com.github.tangyi.api.user.attach.AttachmentStorage;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

	public AttachmentManagerImpl(AttachmentService attachmentService, AttachGroupService groupService,
			LocalAttachmentStorage localStorage, QiNiuAttachmentStorage qiNiuStorage) {
		this.attachmentService = attachmentService;
		this.groupService = groupService;
		this.qiNiuStorage = qiNiuStorage;
		this.register(AttachmentConstant.LOCAL, localStorage);
		this.register(AttachmentConstant.QI_NIU, qiNiuStorage);
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

	@Override
	public Attachment upload(MultipartFileUploadContext context) throws IOException {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment upload(BytesUploadContext context) {
		return getManager(context.getGroup()).upload(context);
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
	public String getDownloadUrl(AttachGroup group, String attachName) {
		return getManager(group).getDownloadUrl(group, attachName);
	}

	@Override
	public String getPreviewUrl(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewUrl(id);
	}

	@Override
	public Attachment getPreviewAttachment(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewAttachment(id);
	}

	@Override
	public Long randomAttachmentId(String groupCode) {
		AttachGroup group = groupService.findByGroupCode(groupCode);
		return getManager(group).randomAttachmentId(group.getGroupCode());
	}

	private AttachGroup getAttachGroupByAttachmentId(Long id) {
		Attachment attachment = attachmentService.getNotNullAttachment(id);
		return groupService.findByGroupCode(attachment.getGroupCode());
	}
}
