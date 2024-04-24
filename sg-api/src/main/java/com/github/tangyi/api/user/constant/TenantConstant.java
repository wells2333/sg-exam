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

package com.github.tangyi.api.user.constant;

import com.github.tangyi.common.utils.EnvUtils;

public class TenantConstant {

	public static final String DEFAULT_TENANT_CODE = EnvUtils.getValue("DEFAULT_TENANT_CODE", "gitee");

    /**
     * 待审核
     */
    public static final Integer PENDING_AUDIT = 0;

	/**
	 * 审核通过
	 */
	public static final Integer PASS_AUDIT = 1;

	public static final Integer NOT_INIT = 0;

	public static final Integer INIT_SUCCESS = 2;
}
