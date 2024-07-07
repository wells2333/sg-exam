/*
 * Copyright 2024 The sg-exam authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tangyi.user.service.attach;

import com.alibaba.fastjson.JSON;
import com.github.tangyi.api.user.attach.BytesUploadContext;
import com.github.tangyi.api.user.attach.MultipartFileUploadContext;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.api.user.model.SysAttachmentChunk;
import com.github.tangyi.common.exceptions.CommonException;
import com.github.tangyi.common.oss.config.QiNiuConfig;
import com.github.tangyi.common.utils.JsonMapper;
import com.github.tangyi.user.thread.ExecutorHolder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.storage.*;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QiNiuAttachmentStorage extends AbstractAttachmentStorage {

	private final QiNiuConfig qiNiuConfig;
	private final UploadManager uploadManager;
	private final BucketManager bucketManager;
	private final Client client;
	private String token;

	protected static class QiNiuAttachException extends CommonException {
		public QiNiuAttachException(String msg) {
			super(msg);
		}

		public QiNiuAttachException(Throwable throwable, String msg) {
			super(throwable, msg);
		}
	}

	private Auth getAuth() {
		if (StringUtils.isNotBlank(qiNiuConfig.getAccessKey()) && StringUtils.isNotBlank(qiNiuConfig.getSecretKey())) {
			return Auth.create(qiNiuConfig.getAccessKey(), qiNiuConfig.getSecretKey());
		}
		throw new QiNiuAttachException("QiNiu Auth is null, please check your qiNiuConfig");
	}

	private String getQiNiuToken() {
		return getAuth().uploadToken(qiNiuConfig.getBucket());
	}

	private Client getClient() {
		Configuration configuration = new Configuration(Region.autoRegion());
		return new Client(configuration);
	}

	private String getUrlPrefix(String token, Client client) throws QiniuException {
		List<String> hosts = new ApiQueryRegion(client).request(new ApiQueryRegion.Request(null, token))
				.getDefaultRegionUpHosts();
		if (CollectionUtils.isEmpty(hosts)) {
			throw new QiNiuAttachException("Failed to query region up hosts, token: " + token);
		}
		String host = hosts.get(0);
		if (!host.startsWith("http")) {
			host = "http://" + host;
		}
		return host;
	}

	private String generateUploadId(String urlPrefix, String token, Client client, String key) throws QiniuException {
		ApiUploadV2InitUpload api = new ApiUploadV2InitUpload(client);
		ApiUploadV2InitUpload.Request req = new ApiUploadV2InitUpload.Request(urlPrefix, token).setKey(key);
		ApiUploadV2InitUpload.Response res = api.request(req);
		return res.getUploadId();
	}

	public QiNiuAttachmentStorage(AttachmentService attachmentService, AttachGroupService groupService,
			QiNiuConfig qiNiuConfig, DefaultImageService defaultImageService, ExecutorHolder executorHolder) {
		super(attachmentService, groupService, defaultImageService, executorHolder);
		this.qiNiuConfig = qiNiuConfig;
		Configuration config = new Configuration(Region.autoRegion());
		this.uploadManager = new UploadManager(config);
		this.bucketManager = new BucketManager(getAuth(), config);
		this.token = getQiNiuToken();
		this.client = getClient();
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
	public Attachment prepare(String groupCode, String fileName, String originalFilename, byte[] bytes, String user,
			String tenantCode, String hash) {
		Attachment attachment;
		int retryCount = 3; // 设置重试次数
		while (retryCount > 0) {
			try {
				attachment = super.prepare(groupCode, fileName, originalFilename, bytes, user, tenantCode, hash);
				String urlPrefix = this.getUrlPrefix(token, client);
				String key = this.preUpload(attachment);
				// 先删除已存在的文件
				this.doDelete(key);
				String uploadId = this.generateUploadId(urlPrefix, token, client, key);
				log.info("Generate upload id finished, fileName: {}, uploadId: {}", fileName, uploadId);
				attachment.setUploadId(uploadId);
				return attachment; // 成功则返回
			} catch (Exception e) {
				token = getQiNiuToken();
				if (retryCount == 1) {
					throw new QiNiuAttachException(e, "Failed to generate upload id, fileName: " + fileName);
				} else {
					retryCount--; // 减少重试次数
				}
			}
		}
		return null; // 执行到这里说明重试次数用完，返回 null 或者其他适当的处理
	}

	@Override
	public String uploadChunk(com.github.tangyi.api.user.attach.ChunkUploadContext context) {
		try {
			String urlPrefix = getUrlPrefix(token, client);
			String chunkName = getShardName(context.getGroup().getGroupCode(), context.getFilename(), context.getHash(),
					false);
			byte[] chunkDate = context.getMultipartFile().getBytes();
			ApiUploadV2UploadPart.Request req = new ApiUploadV2UploadPart.Request(urlPrefix, token,
					context.getUploadId(), context.getIndex()).setKey(chunkName)
					.setUploadData(chunkDate, 0, chunkDate.length, null);
			ApiUploadV2UploadPart.Response res = new ApiUploadV2UploadPart(client).request(req);
			Map<String, Object> chunkInfo = Maps.newHashMapWithExpectedSize(2);
			chunkInfo.put(PART_NUMBER, context.getIndex());
			chunkInfo.put(PART_ETG, res.getEtag());
			String resJson = JSON.toJSONString(chunkInfo);
			log.info("Upload chunk finished, chunkName: {}, resJson: {}", chunkName, resJson);
			return resJson;
		} catch (Exception ex) {
			throw new QiNiuAttachException(ex,
					"Failed to upload chunk. uploadId: " + context.getUploadId() + ", index: " + context.getIndex());
		}
	}

	@Override
	public Attachment mergeChunks(Attachment prepare, AttachGroup group, List<SysAttachmentChunk> chunks)
			throws CommonException {
		String key = null;
		try {
			key = preUpload(prepare);
			String urlPrefix = getUrlPrefix(token, client);
			String uploadId = prepare.getUploadId();
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(chunks), "chunks is empty.");
			Preconditions.checkArgument(StringUtils.isNotEmpty(uploadId), "uploadId is empty.");
			List<Map<String, Object>> chunksInfo = chunks.stream()
					.map(chunk -> JSON.parseObject(chunk.getChunkUploadRes())).collect(Collectors.toList());
			ApiUploadV2CompleteUpload api = new ApiUploadV2CompleteUpload(client);
			ApiUploadV2CompleteUpload.Request req = new ApiUploadV2CompleteUpload.Request(urlPrefix, token, uploadId,
					chunksInfo).setKey(key).setFileName(prepare.getAttachName()).setCustomParam(Collections.emptyMap());
			ApiUploadV2CompleteUpload.Response res = api.request(req);
			if (res != null) {
				prepare.setUrl(this.getDownloadUrl(key, -1));
				log.info("Merge chunks finished, key: {}, res: {}", key, JSON.toJSONString(res));
			}
		} catch (Exception e) {
			log.error("Failed to merge chunks, key: {}", key, e);
		}
		return prepare;
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
		Attachment attachment = this.prepare(groupCode, fileName, originalFilename, bytes, user, tenantCode, hash);
		this.upload(attachment, bytes);
		return attachment;
	}

	@Transactional
	public void upload(Attachment attachment, byte[] bytes) {
		long startNs = System.nanoTime();
		try {
			String fileName = preUpload(attachment);
			Response response = this.uploadManager.put(bytes, fileName, getQiNiuToken());
			DefaultPutRet putRet = JsonMapper.getInstance().fromJson(response.bodyString(), DefaultPutRet.class);
			long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
			log.info("Upload file finished, fileName: {}, took: {}ms", fileName, took);
			String result = qiNiuConfig.getDomainOfBucket() + "/" + putRet.key;
			attachment.setUploadResult(result);
			attachment.setUrl(getDownloadUrl(fileName, AttachmentConstant.DEFAULT_EXPIRE_SECOND));
			if (attachment.getId() == null) {
				attachmentService.insert(attachment);
			} else {
				attachmentService.update(attachment);
			}
		} catch (QiniuException ex) {
			throw new QiNiuAttachException(ex, "Failed to upload file");
		}
	}

	@Override
	public void doDelete(String fileName) throws IOException {
		try {
			log.info("Start to delete file: {}", fileName);
			this.bucketManager.delete(qiNiuConfig.getBucket(), fileName);
			log.info("Delete file finished: {}", fileName);
		} catch (QiniuException e) {
			if (StringUtils.contains(e.getMessage(), "no such file or directory")) {
				log.warn("Delete attachment failed: no such file or directory, fileName: {}", fileName);
			} else {
				throw e;
			}
		}
	}

	@Override
	public String getDownloadUrl(String fileName, long expire) {
		if (expire <= 0) {
			expire = AttachmentConstant.DEFAULT_EXPIRE_SECOND;
		}
		String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
		String publicUrl = String.format("%s/%s", qiNiuConfig.getDomainOfBucket(), encodedFileName);
		return getAuth().privateDownloadUrl(publicUrl, expire);
	}
}
