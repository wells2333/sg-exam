package com.github.tangyi.user.speech;

import com.github.tangyi.api.user.model.Attachment;
import lombok.Data;

/**
 *
 * @author tangyi
 * @date 2022/6/15 8:54 上午
 */
@Data
public class SynthesisHandlerContext {

	private String groupCode;

	private String fileName;

	private String tenantCode;

	private String sysCode;

	private String user;

	private Attachment attachment;
}
