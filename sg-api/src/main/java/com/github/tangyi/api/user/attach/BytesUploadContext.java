package com.github.tangyi.api.user.attach;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BytesUploadContext extends UploadContext {

	private String fileName;

	private String originalFilename;

	private byte[] bytes;

	public BytesUploadContext fileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public BytesUploadContext originalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
		return this;
	}

	public BytesUploadContext fileName(byte[] bytes) {
		this.bytes = bytes;
		return this;
	}
}
