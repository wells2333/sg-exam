package com.github.tangyi.user.service.attach;

import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.FileUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.config.MinioConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.micrometer.core.instrument.util.IOUtils;
import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioAttachmentStorage extends AbstractAttachmentStorage {

    private final MinioConfig minioConfig;

    private final Map<String, String> contentTypeMap;

    private MinioClient minioClient;

    public MinioAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
                                  MinioConfig minioConfig, DefaultImageService defaultImageService, ExecutorHolder executorHolder) {
        super(attachmentService, groupService, defaultImageService, executorHolder);
        this.minioConfig = minioConfig;
        this.contentTypeMap = Maps.newHashMap();
        this.initContentTypes();
        this.initMinioClient();
    }

    private void initContentTypes() {
        try {
            long start = System.nanoTime();
            String str = IOUtils.toString(ResourceUtil.getStream("content_type.json"), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSON.parseObject(str);
            if (jsonObject != null) {
                for (String key : jsonObject.keySet()) {
                    contentTypeMap.put(key, jsonObject.getString(key));
                }
            }
            long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("Init content type finished, size: {}, took: {}ms", contentTypeMap.size(), took);
        } catch (Exception e) {
            log.error("Failed to init content types", e);
        }
    }

    private void initMinioClient() {
        if (!minioConfig.isEnabled()) {
            log.warn("MinIO is disabled");
            return;
        }
        if (StringUtils.isEmpty(minioConfig.getAccessKey())) {
            log.error("MinIO accessKey is empty");
            return;
        }

        if (StringUtils.isEmpty(minioConfig.getSecretKey())) {
            log.error("MinIO secretKey is empty");
            return;
        }
        try {
            this.minioClient = MinioClient.builder().endpoint(minioConfig.getEndpoint())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
            if (StringUtils.isNotEmpty(minioConfig.getBucket())) {
                checkOrCreateBucket(minioConfig.getBucket());
            }
        } catch (Exception e) {
            log.error("Failed to init MinIO client", e);
        }
    }

    private void checkOrCreateBucket(String bucket) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                log.info("Start to make bucket: {}", bucket);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("Make bucket finish, bucket: {}", bucket);
            }
        } catch (Exception e) {
            log.error("Failed to check or create bucket: {}", bucket, e);
        }
    }

    @Override
    @Transactional
    public Attachment upload(MultipartFileUploadContext context) throws IOException {
        String groupCode = context.getGroup().getGroupCode();
        MultipartFile file = context.getMultipartFile();
        return this.upload(groupCode, file.getOriginalFilename(), file.getOriginalFilename(), file.getBytes(),
                context.getUser(), context.getTenantCode(), context.getHash());
    }

    @Override
    @Transactional
    public Attachment upload(FileUploadContext context) throws OssException {
        StopWatch watch = StopWatchUtil.start();
        String groupCode = context.getGroup().getGroupCode();
        File targetFile = context.getTargetFile();
        Attachment attachment = prepare(groupCode, targetFile.getName(), targetFile.getName(), null, context.getUser(),
                context.getTenantCode(), context.getHash());
        String key = preUpload(attachment);
        this.uploadChunks(targetFile, key, context.isDeleteTargetFileAfterUploaded());
        doAfterUpload(attachment, key, key, watch);
        return attachment;
    }

    @Override
    @Transactional
    public Attachment upload(BytesUploadContext context) {
        String groupCode = context.getGroup().getGroupCode();
        return this.upload(groupCode, context.getFileName(), context.getOriginalFilename(), context.getBytes(),
                context.getUser(), context.getTenantCode(), context.getHash());
    }

    @Transactional
    public Attachment upload(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
                             String tenantCode, String hash) {
        Attachment attachment = prepare(groupCode, fileName, originalFilename, bytes, user, tenantCode, hash);
        upload(attachment, bytes);
        return attachment;
    }

    @Transactional
    public void upload(Attachment attachment, byte[] bytes) {
        String fileName = preUpload(attachment);
        String contentType = getContentType(fileName);
        StopWatch watch = StopWatchUtil.start();
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            PutObjectArgs args = PutObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName)
                    .stream(in, in.available(), -1).contentType(contentType).build();
            ObjectWriteResponse response = minioClient.putObject(args);
            doAfterUpload(attachment, response.object(), fileName, watch);
        } catch (Exception ex) {
            throw new OssException(ex, "Failed to upload file");
        }
    }

    @Transactional
    public void doAfterUpload(Attachment attachment, String key, String fileName, StopWatch watch) {
        String took = StopWatchUtil.stop(watch);
        log.info("Upload file done successfully, fileName: {}, took: {}", fileName, took);
        String result = minioConfig.getBucket() + "/" + key;
        attachment.setUploadResult(result);
        attachment.setUrl(getDownloadUrl(fileName, -1));
        if (attachment.getId() == null) {
            attachmentService.insert(attachment);
        } else {
            attachmentService.update(attachment);
        }
    }

    @Override
    public ChunkUploadResponse doUploadChunks(File targetFile, String key) throws OssException {
        try {
            ChunkUploadContext context = new MinioChunkUploadContext().setTargetFile(targetFile).setKey(key)
                    .setContentType(getContentType(targetFile.getName()))
                    .setUploadChunkSizeMb(minioConfig.getChunkSizeMb());
            List<Map<String, Object>> chunksInfo = uploadChunks(context);
            List<ComposeSource> sources = Lists.newArrayListWithExpectedSize(chunksInfo.size());
            List<String> tempFiles = Lists.newArrayListWithExpectedSize(chunksInfo.size());
            for (Map<String, Object> chunk : chunksInfo) {
                String fileName = String.valueOf(chunk.get(PART_NUMBER));
                sources.add(ComposeSource.builder().bucket(minioConfig.getBucket()).object(fileName).build());
                tempFiles.add(fileName);
            }
            ObjectWriteResponse res = minioClient.composeObject(
                    ComposeObjectArgs.builder().bucket(minioConfig.getBucket()).object(key).sources(sources)
                            .headers(Collections.singletonMap("Content-Type", context.getContentType())).build());
            if (res != null && CollectionUtils.isNotEmpty(tempFiles)) {
                CompletableFuture.runAsync(() -> {
                    log.info("Start to delete temp chunk files: {}", tempFiles);
                    for (String fileName : tempFiles) {
                        this.doDelete(null, fileName);
                    }
                    log.info("Delete temp chunk files finished, files: {}", tempFiles);
                });
            }
            return new ChunkUploadResponse(res);
        } catch (Exception ex) {
            throw new OssException(ex, ex.getMessage());
        }
    }

    @Override
    public Map<String, Object> doUploadChunk(ChunkUploadContext context, byte[] chunkData, int chunkNumber)
            throws OssException {
        long start = System.nanoTime();
        Map<String, Object> chunkInfo = Maps.newHashMapWithExpectedSize(2);
        String chunkName = context.getKey() + "_chunk_" + chunkNumber;
        try (InputStream in = new ByteArrayInputStream(chunkData)) {
            PutObjectArgs args = PutObjectArgs.builder().bucket(minioConfig.getBucket()).object(chunkName)
                    .stream(in, in.available(), -1).contentType(context.getContentType()).build();
            ObjectWriteResponse response = minioClient.putObject(args);
            chunkInfo.put(PART_NUMBER, chunkName);
            chunkInfo.put(PART_ETG, response.etag());
            long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("Upload chunk finished, key: {}, chunkName: {}, took: {}ms, etag: {}", context.getKey(), chunkName,
                    took, response.etag());
            return chunkInfo;
        } catch (Exception ex) {
            throw new OssException(ex, "Failed to upload chunk file");
        }
    }

    @Override
    public void doDelete(Attachment attachment, String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName).build());
            log.info("Attachment has been removed, fileName: {}", fileName);
        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(), "no such file or directory")) {
                log.warn("Delete attachment failed: no such file or directory, fileName: {}", fileName);
            } else {
                throw new CommonException(e);
            }
        }
    }

    @Override
    public String getDownloadUrl(String fileName, long expire) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(minioConfig.getBucket()).object(fileName).build());
            if (expire > 0 && expire < (7 * 24 * 3600)) {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioConfig.getBucket())
                                .object(fileName).expiry((int) expire, TimeUnit.SECONDS).build());
            } else {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(minioConfig.getBucket())
                                .object(fileName).build());
            }
        } catch (Exception e) {
            log.error("Failed to get download url, fileName: {}", fileName, e);
        }
        return null;
    }

    private String getContentType(String fileName) {
        String ext = "." + FilenameUtils.getExtension(fileName);
        String contentType = this.contentTypeMap.get(ext);
        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    static class MinioChunkUploadContext extends ChunkUploadContext {

    }
}
