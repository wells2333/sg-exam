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

package com.github.tangyi.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "sys")
public class SysProperties {

    /**
     * 默认头像
     */
    private String defaultAvatar;

    /**
     * 密码加密解密的 key
     */
    private String key;

    private String gatewaySecret;

    /**
     * logo 的后缀名
     */
    private String logoSuffix;

    /**
     * 二维码生成链接
     */
    private String qrCodeUrl;

    /**
     * 附件上传目录
     */
    private String attachPath;

    /**
     * 支持预览的附件后缀名，多个用逗号隔开，如：png,jpeg
     */
    private String canPreview;

	private String publicBucket;

	private String privateBucket;

	private List<String> loadConfigs;
}
