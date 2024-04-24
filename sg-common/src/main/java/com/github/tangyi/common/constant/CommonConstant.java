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

package com.github.tangyi.common.constant;

import com.github.tangyi.common.utils.EnvUtils;

public interface CommonConstant {

	String DEFAULT_PASSWORD = EnvUtils.getValue("DEFAULT_PASSWORD", "123456");

	Integer STATUS_NORMAL = 0;

	Integer DEL_FLAG_NORMAL = 0;

	String VERIFICATION_CODE_KEY = "VERIFICATION_CODE_KEY_";

	String GRANT_TYPE_PASSWORD = "password";

	String TENANT_CODE_HEADER = "Tenant-Code";

	Long ROOT = -1L;

	String COMMA = ",";

	String TENANT_CODE = "tenantCode";
}

