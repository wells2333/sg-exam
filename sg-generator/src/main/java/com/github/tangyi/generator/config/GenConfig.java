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

package com.github.tangyi.generator.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gen")
@ConditionalOnExpression("!'${gen}'.isEmpty()")
public class GenConfig {

	/** 作者 */
	public String author;

	/** 生成包路径 */
	public String packageName;

	/** 生成文件路径 */
	public String genDir;

	/** 自动去除表前缀，默认是 false */
	public boolean autoRemovePre;

	/** 表前缀 (类名不会包含表前缀) */
	public String tablePrefix;
}
