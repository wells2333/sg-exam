package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.FileUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.oss.exceptions.OssException;
import com.github.tangyi.common.properties.SysProperties;
import com.github.tangyi.common.utils.EnvUtils;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.common.utils.StopWatchUtil;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class QiNiuAttachmentStorage extends AbstractAttachmentStorage {

    private static final int QI_NIU_UPLOAD_CHUNK_SIZE_MB = EnvUtils.getInt("QI_NIU_UPLOAD_CHUNK_SIZE_MB", 10);

    private final SysProperties props;

    private final QiNiuConfig qiNiuConfig;

    public QiNiuAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
                                  SysProperties props, QiNiuConfig qiNiuConfig,
                                  DefaultImageService defaultImageService, ExecutorHolder executorHolder) {
        super(attachmentService, groupService, defaultImageService, executorHolder);
        this.props = props;
        this.qiNiuConfig = qiNiuConfig;
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
        Attachment attachment = prepare(groupCode, targetFile.getName(), targetFile.getName(), null,
                context.getUser(), context.getTenantCode(), context.getHash());
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
        try {
            String fileName = preUpload(attachment);
            StopWatch watch = StopWatchUtil.start();
            Response response = getUploadManager().put(bytes, fileName, getQiNiuToken());
            DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
            doAfterUpload(attachment, putRet.key, fileName, watch);
        } catch (QiniuException ex) {
            throw new OssException(ex, "Failed to upload file");
        }
    }

    @Transactional
    public void doAfterUpload(Attachment attachment, String key, String fileName, StopWatch watch) {
        String took = StopWatchUtil.stop(watch);
        log.info("Upload file done successfully, fileName: {}, took: {}", fileName, took);
        String result = qiNiuConfig.getDomainOfBucket() + "/" + key;
        attachment.setUploadResult(result);
        attachment.setUrl(getDownloadUrl(fileName, AttachmentConstant.DEFAULT_EXPIRE_SECOND));
        if (attachment.getId() == null) {
            attachmentService.insert(attachment);
        } else {
            attachmentService.update(attachment);
        }
    }

    @Override
    public ChunkUploadResponse doUploadChunks(File targetFile, String key) throws OssException {
        String fileName = targetFile.getName();
        String token = getQiNiuToken();
        Client client = getClient();
        try {
            String urlPrefix = getUrlPrefix(token, client);
            // 初始化 uploadId
            String uploadId = initUploadId(urlPrefix, token, client, key);
            ChunkUploadContext context = new QiNiuChunkUploadContext()
                    .setUrlPrefix(urlPrefix)
                    .setToken(token)
                    .setClient(client).setUploadId(uploadId)
                    .setTargetFile(targetFile)
                    .setKey(key)
                    .setUploadChunkSizeMb(QI_NIU_UPLOAD_CHUNK_SIZE_MB);
            // 上传文件分片
            List<Map<String, Object>> chunksInfo = uploadChunks(context);
            // 合并文件分片
            Map<String, Object> param = Maps.newHashMap();
            ApiUploadV2CompleteUpload api = new ApiUploadV2CompleteUpload(client);
            ApiUploadV2CompleteUpload.Request req = new ApiUploadV2CompleteUpload.Request(urlPrefix, token, uploadId,
                    chunksInfo).setKey(key).setFileName(fileName).setCustomParam(param);
            return new ChunkUploadResponse(api.request(req));
        } catch (Exception e) {
            throw new OssException(e, e.getMessage());
        }
    }

    private String getUrlPrefix(String token, Client client) throws QiniuException {
        ApiQueryRegion api = new ApiQueryRegion(client);
        ApiQueryRegion.Response res = api.request(new ApiQueryRegion.Request(null, token));
        List<String> hosts = res.getDefaultRegionUpHosts();
        if (CollectionUtils.isEmpty(hosts)) {
            throw new CommonException("Failed to query region up hosts");
        }
        String host = hosts.get(0);
        if (!host.startsWith("http")) {
            host = "http://" + host;
        }
        return host;
    }

    /**
     * 初始化上传，返回 uploadId
     */
    private String initUploadId(String urlPrefix, String token, Client client, String key) throws QiniuException {
        ApiUploadV2InitUpload api = new ApiUploadV2InitUpload(client);
        ApiUploadV2InitUpload.Request req = new ApiUploadV2InitUpload.Request(urlPrefix, token).setKey(key);
        ApiUploadV2InitUpload.Response res = api.request(req);
        return res.getUploadId();
    }

    @Override
    public Map<String, Object> doUploadChunk(ChunkUploadContext chunkUploadContext, byte[] chunkData,
                                             int chunkNumber) throws OssException {
        try {
            long start = System.nanoTime();
            QiNiuChunkUploadContext context = (QiNiuChunkUploadContext) chunkUploadContext;
            ApiUploadV2UploadPart api = new ApiUploadV2UploadPart(context.getClient());
            ApiUploadV2UploadPart.Request req = new ApiUploadV2UploadPart.Request(context.getUrlPrefix(),
                    context.getToken(),
                    context.getUploadId(), chunkNumber).setKey(context.getKey()).setUploadData(chunkData, 0,
                    chunkData.length, null);
            ApiUploadV2UploadPart.Response res = api.request(req);
            String etag = res.getEtag();
            Map<String, Object> chunkInfo = Maps.newHashMap();
            chunkInfo.put(PART_NUMBER, chunkNumber);
            chunkInfo.put(PART_ETG, etag);
            long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("Upload chunk finished, key: {}, chunkNumber: {}, uploadId: {}, took: {}ms, etag: {}",
                    context.getKey(), chunkNumber,
                    context.getUploadId(), took, etag);
            return chunkInfo;
        } catch (Exception e) {
            throw new OssException(e, e.getMessage());
        }
    }

    @Override
    public void doDelete(Attachment attachment, String fileName) throws IOException {
        try {
            BucketManager manager = getBucketManager(getAuth(), new Configuration(Region.region2()));
            manager.delete(qiNiuConfig.getBucket(), fileName);
        } catch (QiniuException e) {
            if (StringUtils.contains(e.getMessage(), "no such file or directory")) {
                log.warn("Delete attachment failed: no such file or directory, fileName: {}", fileName);
            } else {
                throw e;
            }
        }
    }

    public String getDownloadUrl(String fileName, long expire) {
        if (expire <= 0) {
            expire = AttachmentConstant.DEFAULT_EXPIRE_SECOND;
        }
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
        return getAuth().privateDownloadUrl(publicUrl, expire);
    }

    private Auth getAuth() {
        if (StringUtils.isNotBlank(qiNiuConfig.getAccessKey()) && StringUtils.isNotBlank(qiNiuConfig.getSecretKey())) {
            return Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
        }
        throw new IllegalArgumentException("QiNiu Auth is null, please check your qiNiuConfig");
    }

    private UploadManager getUploadManager() {
        Configuration config = new Configuration(Region.autoRegion());
        return new UploadManager(config);
    }

    private BucketManager getBucketManager(Auth auth, Configuration config) {
        return new BucketManager(auth, config);
    }

    private String getQiNiuToken() {
        return getAuth().uploadToken(qiNiuConfig.getBucket());
    }

    private Client getClient() {
        Configuration configuration = new Configuration(Region.autoRegion());
        return new Client(configuration);
    }

    @Data
    @ToString
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    static class QiNiuChunkUploadContext extends ChunkUploadContext {

        private Client client;

        private String urlPrefix;

        private String token;

        private String uploadId;
    }
}
