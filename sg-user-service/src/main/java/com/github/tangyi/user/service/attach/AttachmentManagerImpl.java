package com.github.tangyi.user.service.attach;

import com.github.tangyi.api.user.attach.*;
import com.github.tangyi.api.user.constant.AttachmentConstant;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.api.user.model.Attachment;
import com.github.tangyi.common.base.SgPreconditions;
import com.github.tangyi.common.utils.SysUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AttachmentManagerImpl implements AttachmentManager {

	private static final String CHUNK_TMP_DIR =
			System.getProperty("java.io.tmpdir") + "sg/attach/hash/%s" + File.separator;

	private final Map<Integer, AttachmentStorage> storageMap = Maps.newHashMap();

	private final AttachmentService attachmentService;

	private final AttachGroupService groupService;

	private final QiNiuAttachmentStorage qiNiuStorage;

	public AttachmentManagerImpl(AttachmentService attachmentService, AttachGroupService groupService,
			LocalAttachmentStorage localStorage, QiNiuAttachmentStorage qiNiuStorage) {
		this.attachmentService = attachmentService;
		this.groupService = groupService;
		this.qiNiuStorage = qiNiuStorage;
		this.register(AttachmentConstant.LOCAL, localStorage);
		this.register(AttachmentConstant.QI_NIU, qiNiuStorage);
	}

	private void register(Integer storageType, AttachmentStorage storage) {
		SgPreconditions.checkNull(storageType, "storageType must not be null");
		SgPreconditions.checkNull(storage, "storage must not be null");
		storageMap.put(storageType, storage);
	}

	private AttachmentStorage getManager(AttachGroup group) {
		SgPreconditions.checkNull(group, "AttachGroup must not be null");
		AttachmentStorage storage = this.storageMap.get(group.getStorageType());
		if (storage == null) {
			log.warn("storage not found, storageType: {}, use qiNiuStorage", group.getStorageType());
			return qiNiuStorage;
		}
		return storage;
	}

	@Override
	public Attachment upload(MultipartFileUploadContext context) throws IOException {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment upload(FileUploadContext context) throws IOException, ExecutionException, InterruptedException {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment upload(BytesUploadContext context) {
		return getManager(context.getGroup()).upload(context);
	}

	@Override
	public Attachment prepareUploadChunks(AttachGroup group, Attachment attachment) {
		String hash = attachment.getHash();
		SgPreconditions.checkBlank(hash, "hash must not be null");
		String tenantCode = SysUtil.getTenantCode();
		Attachment dbAttachment = attachmentService.findByHash(hash, tenantCode);
		if (dbAttachment == null) {
			AttachmentStorage storage = getManager(group);
			String user = SysUtil.getUser();
			Attachment prepare = storage.prepareAttachment(group.getGroupCode(), attachment.getAttachName(),
					attachment.getAttachName(), null, user, tenantCode);
			prepare.setHash(attachment.getHash());
			attachmentService.insert(prepare);
			log.info("Prepare upload chunks finished, hash: {}, id: {}", hash, prepare.getId());
			return prepare;
		}
		return dbAttachment;
	}

	@Override
	public Boolean uploadChunk(ChunkUploadContext context) throws IOException {
		MultipartFile multipartFile = context.getMultipartFile();
		String hash = context.getHash();
		Integer index = context.getIndex();
		SgPreconditions.checkNull(multipartFile, "Chunk file must not be null");
		SgPreconditions.checkBlank(hash, "Chunk file hash must not be null");
		SgPreconditions.checkNull(index, "Chunk file index must not be null");
		String tenantCode = SysUtil.getTenantCode();
		Attachment prepare = attachmentService.findByHash(hash, tenantCode);
		SgPreconditions.checkNull(prepare, "Chunks are not prepare");
		String filename = prepare.getAttachName();
		String hashDir = getHashDir(hash);
		File hashDirFile = new File(hashDir);
		if (!hashDirFile.exists()) {
			FileUtils.forceMkdir(hashDirFile);
		}
		if (hashDirFile.exists()) {
			// 按数字重新命名文件
			File targetFile = new File(hashDir + index);
			if (targetFile.exists()) {
				log.info("The chunk file already exists, start to delete it, hash: {}, filename: {}, index: {}", hash,
						filename, index);
				FileUtils.deleteQuietly(targetFile);
			}
			FileCopyUtils.copy(multipartFile.getBytes(), targetFile);
			log.info("Upload chunk file finished, hash: {}, filename: {}, index: {}", hash, filename, index);
			return targetFile.exists();
		}
		return Boolean.FALSE;
	}

	@Override
	public Attachment mergeChunks(String hash) throws IOException, ExecutionException, InterruptedException {
		String tenantCode = SysUtil.getTenantCode();
		Attachment prepare = attachmentService.findByHash(hash, tenantCode);
		SgPreconditions.checkNull(prepare, "Chunks are not prepare");
		String hashDir = getHashDir(hash);
		File hashDirFile = new File(hashDir);
		if (!hashDirFile.exists()) {
			return null;
		}
		List<File> files = getFilesAndSort(hashDirFile);
		if (CollectionUtils.isNotEmpty(files)) {
			AttachGroup group = groupService.findByGroupCode(prepare.getGroupCode());
			return mergeAndUploadChunks(prepare, group, hash, hashDir, files, tenantCode);
		}
		return null;
	}

	@Override
	public boolean delete(Attachment attachment) throws IOException {
		AttachGroup group = groupService.findByGroupCode(attachment.getGroupCode());
		return getManager(group).delete(attachment);
	}

	@Override
	public boolean deleteAll(AttachGroup group, List<Attachment> attachments) throws IOException {
		return getManager(group).deleteAll(attachments);
	}

	@Override
	public String getDownloadUrl(AttachGroup group, String attachName) {
		return getManager(group).getDownloadUrl(group, attachName);
	}

	@Override
	public String getPreviewUrl(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewUrl(id);
	}

	@Override
	public Attachment getPreviewAttachment(Long id) {
		AttachGroup group = getAttachGroupByAttachmentId(id);
		return getManager(group).getPreviewAttachment(id);
	}

	@Override
	public Long randomAttachmentId(String groupCode) {
		AttachGroup group = groupService.findByGroupCode(groupCode);
		return getManager(group).randomAttachmentId(group.getGroupCode());
	}

	private AttachGroup getAttachGroupByAttachmentId(Long id) {
		Attachment attachment = attachmentService.getNotNullAttachment(id);
		return groupService.findByGroupCode(attachment.getGroupCode());
	}

	private Attachment mergeAndUploadChunks(Attachment prepare, AttachGroup group, String hash, String hashDir,
			List<File> files, String tenantCode) throws IOException, ExecutionException, InterruptedException {
		File targetFile = mergeChunks(prepare, hash, hashDir, files);
		if (targetFile.exists()) {
			log.info("Start to upload merged file, hash: {}", hash);
			long start = System.nanoTime();
			String user = SysUtil.getUser();
			AttachmentStorage storage = getManager(group);
			FileUploadContext context = new FileUploadContext();
			context.group(group).user(user).tenantCode(tenantCode);
			context.setTargetFile(targetFile);
			Attachment attachment = storage.upload(context);
			long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
			log.info("Upload merged file finished, hash: {}, took: {}ms", hash, took);
			return attachment;
		}
		return null;
	}

	private File mergeChunks(Attachment prepare, String hash, String hashDir, List<File> files) throws IOException {
		File targetFile = new File(hashDir + prepare.getAttachName());
		if (targetFile.exists()) {
			FileUtils.deleteQuietly(targetFile);
		}
		long startNano = System.nanoTime();
		try (FileOutputStream outputStream = new FileOutputStream(targetFile);
				FileChannel outChannel = outputStream.getChannel()) {
			long start = 0;
			for (File file : files) {
				long startChunkNano = System.nanoTime();
				log.info("Start to merge chunk, hash: {}, filename: {}", hash, file.getName());
				try (FileInputStream in = new FileInputStream(file); FileChannel inChannel = in.getChannel()) {
					outChannel.transferFrom(inChannel, start, file.length());
					start += file.length();
				}
				long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startChunkNano);
				log.info("Merge chunk finished, hash: {}, filename: {}, took: {}ms", hash, file.getName(), took);
			}
		}
		long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano);
		log.info("Merge all chunk finished, fileName: {}, hash: {}, took: {}ms", targetFile.getName(), hash, took);
		return targetFile;
	}

	private String getHashDir(String hash) {
		return String.format(CHUNK_TMP_DIR, hash);
	}

	private List<File> getFilesAndSort(File hashDirFile) {
		File[] files = hashDirFile.listFiles(pathname -> StringUtils.isNumber(pathname.getName()));
		if (ArrayUtils.isNotEmpty(files)) {
			List<File> result = Arrays.asList(files);
			if (result.size() > 1) {
				result.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName())));
			}
			return result;
		}
		return Collections.emptyList();
	}
}
