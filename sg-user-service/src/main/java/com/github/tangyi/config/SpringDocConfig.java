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

package com.github.tangyi.config;

import com.github.tangyi.common.utils.EnvUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

	private static final String SECURITY_SCHEME_NAME = "BearerAuth";
	private static final String TENANT_CODE = "Tenant-Code";
	private static final String NAME = EnvUtils.getValue("DOC_NAME", "SG-EXAM");

	@Bean
	public OpenAPI openApi() {
		return new OpenAPI().info(new Info()    //
						.title(NAME)    //
						.description(NAME)    //
						.version("0.0.12")    //
						.license(new License().name("Apache 2.0").url("https://gitee.com/wells2333")))    //
				.externalDocs(new ExternalDocumentation().description(NAME).url("https://www.yunmianshi.com/"))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME).addList(TENANT_CODE))
				.components(new Components()    //
						.addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()    //
								.name(SECURITY_SCHEME_NAME)    //
								.type(SecurityScheme.Type.HTTP)    //
								.scheme("bearer")    //
								.bearerFormat("JWT"))    //
						.addSecuritySchemes(TENANT_CODE, new SecurityScheme()    //
								.name(TENANT_CODE)    //
								.type(SecurityScheme.Type.APIKEY)    //
								.in(SecurityScheme.In.HEADER)));
	}
}
