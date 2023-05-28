package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.AttachmentStorage;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.constant.Group;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.FileUtil;
import com.github.tangyi.common.utils.HashUtil;
import com.github.tangyi.constants.UserCacheName;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractAttachmentStorage implements AttachmentStorage {

    static final String PART_ETG = "etag";

    static final String PART_NUMBER = "partNumber";

    private static final int ATTACHMENT_STORAGE_SHARD_SIZE = EnvUtils.getInt("ATTACHMENT_STORAGE_SHARD_SIZE", 256);

    protected final AttachmentService attachmentService;

    protected final AttachGroupService groupService;

    protected final DefaultImageService defaultImageService;

    protected final ExecutorHolder executorHolder;

    public AbstractAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
                                     DefaultImageService defaultImageService,
                                     ExecutorHolder executorHolder) {
        this.attachmentService = attachmentService;
        this.groupService = groupService;
        this.defaultImageService = defaultImageService;
        this.executorHolder = executorHolder;
    }

    @Override
    public Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
                              String tenantCode) {
        SgPreconditions.checkNull(groupCode, "groupCode is null");
        Attachment attachment = new Attachment();
        attachment.setCommonValue(user, tenantCode);
        attachment.setAttachType(FileUtil.getFileNameEx(fileName));
        if (bytes != null) {
            attachment.setAttachSize(String.valueOf(bytes.length));
        }
        attachment.setAttachName(originalFilename);
        attachment.setGroupCode(groupCode);
        return attachment;
    }

    protected String preUpload(Attachment attachment) {
        SgPreconditions.checkNull(attachment, "attachment is null");
        SgPreconditions.checkNull(attachment.getGroupCode(), "groupCode must not null");
        // groupCode 作为目录
        return getShardName(attachment.getGroupCode(), attachment.getAttachName());
    }

    @Transactional
    @CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, key = "#attachment.id")
    public boolean delete(Attachment attachment) throws IOException {
        SgPreconditions.checkNull(attachment, "attachment is null");
        if (attachmentService.delete(attachment) > 0 && attachment.getGroupCode() != null) {
            AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
            if (group != null) {
                String fileName = getShardName(group.getGroupCode(), attachment.getAttachName());
                log.info("Deleting file {} ...", fileName);
                doDelete(attachment, fileName);
                log.info("File has been deleted, fileName: {}", fileName);
                return true;
            }
        }
        return false;
    }

    @Transactional
    @CacheEvict(value = {UserCacheName.ATTACHMENT, UserCacheName.ATTACHMENT_URL}, allEntries = true)
    public boolean deleteAll(List<Attachment> attachments) throws IOException {
        boolean result = false;
        for (Attachment attachment : attachments) {
            if (isNotDefaultGroup(attachment)) {
                result = this.delete(attachment);
            }
        }
        return result;
    }

    public String getDownloadUrl(AttachGroup group, String attachName) {
        String fileName = getShardName(group.getGroupCode(), attachName);
        return getDownloadUrl(fileName, group.getUrlExpire());
    }

    @Cacheable(value = UserCacheName.ATTACHMENT_URL, key = "#id", unless = "#result == null")
    public String getPreviewUrl(Long id) {
        Attachment attachment = getPreviewAttachment(id);
        return attachment != null ? attachment.getUrl() : null;
    }

    public Attachment getPreviewAttachment(Long id) {
        Attachment attachment = attachmentService.get(id);
        if (attachment != null && StringUtils.isEmpty(attachment.getUrl())) {
            AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
            if (group != null) {
                String fileName = getShardName(group.getGroupCode(), attachment.getAttachName());
                String url = getDownloadUrl(fileName, group.getUrlExpire());
                attachment.setUrl(url);
            }
        }
        return attachment;
    }

    public abstract void doDelete(Attachment attachment, String fileName) throws IOException;

    public abstract ChunkUploadResponse doUploadChunks(File targetFile, String key) throws OssException;

    public abstract Map<String, Object> doUploadChunk(ChunkUploadContext context, byte[] chunkData, int chunkNumber) throws OssException;

    protected String getShardName(String groupCode, String fileName) {
        if (groupCode != null) {
            int shardId = HashUtil.getShardId(fileName, ATTACHMENT_STORAGE_SHARD_SIZE);
            return getName(groupCode, shardId, fileName);
        }
        return fileName;
    }

    protected String getName(String groupCode, String fileName) {
        return getName(groupCode, -1, fileName);
    }

    protected String getName(String groupCode, int shardId, String fileName) {
        String res = groupCode + "/";
        if (shardId > 0) {
            res = res + shardId + "/";
        }
        return res + fileName;
    }

    protected byte[] getChunkData(RandomAccessFile file, long offset, int size) {
        byte[] uploadData = new byte[size];
        try {
            file.seek(offset);
            int readSize = 0;
            while (readSize != size) {
                int s = 0;
                try {
                    s = file.read(uploadData, readSize, size - readSize);
                } catch (IOException e) {
                    log.error("Failed to get upload data", e);
                }
                if (s >= 0) {
                    readSize += s;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            uploadData = null;
        }
        if (uploadData == null) {
            throw new CommonException("Failed to get upload chunk data");
        }
        return uploadData;
    }

    protected List<Map<String, Object>> uploadChunks(ChunkUploadContext context) throws IOException,
            ExecutionException, InterruptedException {
        List<Map<String, Object>> chunkList = Lists.newArrayList();
        try (RandomAccessFile file = new RandomAccessFile(context.getTargetFile(), "r")) {
            ListeningExecutorService executor = executorHolder.getQiNiuUploadExecutor();
            List<Future<Map<String, Object>>> futures = Lists.newArrayList();
            long fileSize = context.getTargetFile().length();
            int defaultChunkSize = 1024 * 1024 * context.getUploadChunkSizeMb();
            long chunkOffset = 0;
            int chunkNumber = 1;
            while (chunkOffset < fileSize) {
                long chunkSize = fileSize - chunkOffset;
                if (chunkSize > defaultChunkSize) {
                    chunkSize = defaultChunkSize;
                }
                byte[] chunkData = getChunkData(file, chunkOffset, (int) chunkSize);
                final int finalChunkNumber = chunkNumber;
                futures.add(executor.submit(() -> doUploadChunk(context, chunkData, finalChunkNumber)));
                chunkNumber += 1;
                chunkOffset += chunkSize;
            }
            for (Future<Map<String, Object>> future : futures) {
                Map<String, Object> chunkInfo = future.get();
                if (chunkInfo != null) {
                    chunkList.add(chunkInfo);
                }
            }
        }
        return chunkList;
    }

    public void uploadChunks(File targetFile, String key, boolean deleteTargetFileAfterUploaded) throws OssException {
        long start = System.nanoTime();
        ChunkUploadResponse res = doUploadChunks(targetFile, key);
        long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        log.info("Upload chunks finished, fileName: {}, key: {}, took: {}ms, response: {}", targetFile.getName(), key
                , took, res);
        checkOrDeleteTargetFile(targetFile, deleteTargetFileAfterUploaded);
    }

    @Override
    @Transactional
    public Long defaultImage(String groupCode) {
        String fileName = defaultImageService.randomImage();
        String url = getDownloadUrl(fileName, AttachmentConstant.DEFAULT_EXPIRE_SECOND);
        Attachment attachment = new Attachment();
        attachment.setCommonValue();
        attachment.setUrl(url);
        attachment.setAttachType(FileUtil.getFileNameEx(url));
        attachment.setAttachName(FileUtil.getFileNameFromUrl(url));
        attachment.setGroupCode(groupCode);
        attachmentService.insert(attachment);
        log.info("Generate random image successfully, groupCode: {}, url: {}, id: {}", groupCode, attachment.getUrl()
                , attachment.getId());
        return attachment.getId();
    }

    private boolean isNotDefaultGroup(Attachment attachment) {
        return !Group.DEFAULT.equals(attachment.getGroupCode());
    }

    protected void checkOrDeleteTargetFile(File targetFile, boolean deleteTargetFileAfterUploaded) {
        if (deleteTargetFileAfterUploaded && targetFile.delete()) {
            log.info("The original file has been deleted, fileName: {}", targetFile.getName());
        }
    }

    record ChunkUploadResponse(Object data) {

    }

    @Data
    @ToString
    @Accessors(chain = true)
    static class ChunkUploadContext {

        private File targetFile;

        private String key;

        private String contentType;

        private int uploadChunkSizeMb;

    }
}
