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
