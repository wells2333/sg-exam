package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.enums.AttachTypeEnum;
import com.github.tangyi.api.user.model.AttachGroup;
import com.github.tangyi.common.utils.SysUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultipartFileUploadContext extends UploadContext {

	private MultipartFile multipartFile;

	public MultipartFileUploadContext multipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
		return this;
	}

	public static MultipartFileUploadContext of(AttachTypeEnum type, MultipartFile file) {
		return of(AttachGroup.of(type), file, SysUtil.getUser(), SysUtil.getTenantCode());
	}

	public static MultipartFileUploadContext of(AttachGroup group, MultipartFile file) {
		return of(group, file, SysUtil.getUser(), SysUtil.getTenantCode());
	}

	public static MultipartFileUploadContext of(AttachGroup group, MultipartFile file, String user, String tenantCode) {
		return (MultipartFileUploadContext) new MultipartFileUploadContext().multipartFile(file).group(group).user(user)
				.tenantCode(tenantCode);
	}
}
