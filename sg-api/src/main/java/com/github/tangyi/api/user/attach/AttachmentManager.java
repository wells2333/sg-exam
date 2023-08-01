package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.exceptions.CommonException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AttachmentManager {

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    Attachment upload(BytesUploadContext context);

    Attachment prepareUploadChunks(AttachGroup group, Attachment attachment);

    Boolean uploadChunk(ChunkUploadContext context) throws IOException;

    Attachment mergeChunks(String hash) throws CommonException;

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getPreviewUrl(Long id);

    Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
