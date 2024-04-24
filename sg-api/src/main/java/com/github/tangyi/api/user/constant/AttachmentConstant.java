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

public class AttachmentConstant {

    public static final Integer QI_NIU = 1;

    public static final Integer MINIO = 2;

    // URL 默认的失效时间，一百年
    public static final long DEFAULT_EXPIRE_SECOND = EnvUtils.getLong("ATTACH_URL_DEFAULT_EXPIRE_SECOND", 33233472000L);
}
