package com.github.tangyi.user.speech;

import com.github.tangyi.api.user.model.Attachment;
import lombok.Data;

@Data
public class SynthesisHandlerContext {

	private String groupCode;

	private String fileName;

	private String tenantCode;

	private String sysCode;

	private String user;

	private Attachment attachment;
}
