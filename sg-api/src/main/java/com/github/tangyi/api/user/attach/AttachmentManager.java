package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;

import java.io.IOException;
import java.util.List;

public interface AttachmentManager {

	Attachment upload(MultipartFileUploadContext context) throws IOException;

	Attachment upload(BytesUploadContext context);

	boolean delete(Attachment attachment) throws IOException;

	boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException;

	String getDownloadUrl(AttachGroup group, String attachName);

	String getPreviewUrl(Long id);

	Attachment getPreviewAttachment(Long id);

	Long randomAttachmentId(String groupCode);
}
