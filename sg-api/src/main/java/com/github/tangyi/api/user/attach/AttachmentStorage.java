package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AttachmentStorage {

	Attachment prepareAttachment(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode);

	Attachment upload(MultipartFileUploadContext context) throws IOException;

	Attachment upload(FileUploadContext context) throws IOException, ExecutionException, InterruptedException;

	Attachment upload(BytesUploadContext context);

	boolean delete(Attachment attachment) throws IOException;

	boolean deleteAll(List<Attachment> attachments) throws IOException;

	String getDownloadUrl(AttachGroup group, String attachName);

	String getDownloadUrl(String fileName, long expire);

	String getPreviewUrl(Long id);

	Attachment getPreviewAttachment(Long id);

	Long randomAttachmentId(String groupCode);
}
