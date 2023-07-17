package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.oss.exceptions.OssException;

import java.io.IOException;
import java.util.List;

public interface AttachmentStorage {

    Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
                       String tenantCode, String hash);

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    Attachment upload(FileUploadContext context) throws OssException;

    Attachment upload(BytesUploadContext context);

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getDownloadUrl(String fileName, long expire);

    String getPreviewUrl(Long id);

    Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
