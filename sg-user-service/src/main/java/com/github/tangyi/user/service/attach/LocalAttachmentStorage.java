package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.user.constants.AttachConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class LocalAttachmentStorage extends AbstractAttachmentStorage {

	private final AttachmentService attachmentService;

	private String localDirectory;

	public LocalAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService) {
		super(attachmentService, groupService);
		this.attachmentService = attachmentService;
		this.localDirectory = EnvUtils.getValue("ATTACHMENT_LOCAL_DIRECTORY");
		if (StringUtils.isEmpty(localDirectory)) {
			// ${user.home}/sg-attachments/
			this.localDirectory = FileUtils.getUserDirectoryPath() + File.separator + "sg-attachments" + File.separator;
		}
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
			String fullName = this.localDirectory + fileName;
			File fullDirectory = new File(FilenameUtils.getFullPathNoEndSeparator(fullName));
			if (!fullDirectory.exists()) {
				FileUtils.forceMkdir(fullDirectory);
			}
			if (!fullDirectory.exists()) {
				throw new IOException("Failed to create  directory: " + fullDirectory.getAbsolutePath());
			}
			FileCopyUtils.copy(bytes, new File(fullName));
			String took = StopWatchUtil.stop(watch);
			log.info("Upload file done successfully, fileName: {}, took: {}", fileName, took);
			attachment.setUploadResult(fullName);
			attachment.setUrl(getDownloadUrl(fileName, AttachConstant.DEFAULT_EXPIRE));
			attachmentService.insert(attachment);
		} catch (Exception ex) {
			log.error("Failed to upload file", ex);
			throw new OssException(ex, "Failed to upload file");
		}
	}

	@Override
	public void doDelete(Attachment attachment, String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			FileUtils.deleteQuietly(new File(fileName));
		}
	}

	@Override
	public String getDownloadUrl(AttachGroup group, String attachName) {
		return null;
	}

	@Override
	public String getDownloadUrl(String fileName, long expire) {
		return "/v1/attachment/download?id";
	}

	@Override
	public String getPreviewUrl(Long id) {
		return "/v1/attachment/download?id" + id;
	}

	@Override
	public Attachment getPreviewAttachment(Long id) {
		return null;
	}

	@Override
	public Long randomAttachmentId(String groupCode) {
		return null;
	}
}
