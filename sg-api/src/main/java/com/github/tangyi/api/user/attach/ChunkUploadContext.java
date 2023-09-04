package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.utils.SysUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChunkUploadContext extends UploadContext {

	private MultipartFile multipartFile;

	private String hash;

	private Integer index;

	private String filename;

	private String uploadId;

	public static ChunkUploadContext of(AttachTypeEnum type, MultipartFile file) {
		return of(AttachGroup.of(type), file, SysUtil.getUser(), SysUtil.getTenantCode());
	}

	public static ChunkUploadContext of(AttachGroup group, MultipartFile file) {
		return of(group, file, SysUtil.getUser(), SysUtil.getTenantCode());
	}

	public static ChunkUploadContext of(AttachGroup group, MultipartFile file, String user, String tenantCode) {
		ChunkUploadContext context = new ChunkUploadContext();
		context.group(group);
		context.setMultipartFile(file);
		context.user(user);
		context.tenantCode(tenantCode);
		return context;
	}

	public ChunkUploadContext hash(String hash) {
		this.hash = hash;
		return this;
	}

	public ChunkUploadContext index(Integer index) {
		this.index = index;
		return this;
	}

	public ChunkUploadContext filename(String filename) {
		this.filename = filename;
		return this;
	}
}
