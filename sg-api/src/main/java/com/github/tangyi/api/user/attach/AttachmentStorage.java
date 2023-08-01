package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.exceptions.OssException;

import java.io.IOException;
import java.rmi.ServerException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface AttachmentStorage {

    Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
                       String tenantCode, String hash);

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    String uploadChunk(ChunkUploadContext context) throws IOException;

    Attachment mergeChunks(Attachment prepare, AttachGroup group, List<SysAttachmentChunk> chunks) throws CommonException;

    Attachment upload(BytesUploadContext context);

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getDownloadUrl(String fileName, long expire);

    String getPreviewUrl(Long id);

    Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
