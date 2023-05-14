package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import lombok.Data;

@Data
public class UploadContext {

	private AttachGroup group;

	private String user;

	private String tenantCode;

	public UploadContext group(AttachGroup group) {
		this.group = group;
		return this;
	}

	public UploadContext user(String user) {
		this.user = user;
		return this;
	}

	public UploadContext tenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
		return this;
	}
}
