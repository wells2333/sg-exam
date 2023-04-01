package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.qiniu.common.QiniuException;

import java.io.IOException;
import java.util.List;

public interface AttachmentStorage {

	Attachment upload(MultipartFileUploadContext context) throws IOException;

	Attachment upload(BytesUploadContext context);

	boolean delete(Attachment attachment) throws QiniuException;

	boolean deleteAll(List<Attachment> attachments) throws QiniuException;

	String getDownloadUrl(AttachGroup group, String attachName);

	String getDownloadUrl(String fileName, long expire);

	String getPreviewUrl(Long id);

	Attachment getPreviewAttachment(Long id);

	Long randomAttachmentId(String groupCode);
}
