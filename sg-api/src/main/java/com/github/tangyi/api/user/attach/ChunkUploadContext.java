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
