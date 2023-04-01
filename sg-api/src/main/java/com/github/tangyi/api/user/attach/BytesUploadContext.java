package com.github.tangyi.api.user.attach;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BytesUploadContext extends UploadContext {

	private String fileName;

	private String originalFilename;

	private byte[] bytes;
}
