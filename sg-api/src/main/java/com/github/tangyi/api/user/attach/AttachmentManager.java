package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AttachmentManager {

    Attachment upload(MultipartFileUploadContext context) throws IOException;

    Attachment upload(FileUploadContext context) throws IOException, ExecutionException, InterruptedException;

    Attachment upload(BytesUploadContext context);

    Attachment prepareUploadChunks(AttachGroup group, Attachment attachment);

    Boolean uploadChunk(ChunkUploadContext context) throws IOException;

    Attachment mergeChunks(String hash) throws IOException;

    boolean delete(Attachment attachment) throws IOException;

    boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException;

    String getDownloadUrl(AttachGroup group, String attachName, String hash);

    String getPreviewUrl(Long id);

    Attachment getPreviewAttachment(Long id);

    Long defaultImage(String groupCode);
}
