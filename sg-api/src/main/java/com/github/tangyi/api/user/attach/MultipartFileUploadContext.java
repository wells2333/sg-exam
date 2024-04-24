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
