package com.github.tangyi.api.user.attach;

import com.github.tangyi.api.user.model.AttachGroup;
import lombok.Data;

@Data
public class UploadContext {

	private AttachGroup group;

	private String user;

	private String tenantCode;
}
