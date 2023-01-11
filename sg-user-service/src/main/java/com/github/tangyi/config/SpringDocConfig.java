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

	private static final String NAME = EnvUtils.getValue("DOC_NAME", "云面试");

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title(NAME).description(NAME).version("v5.0.0")
						.license(new License().name("Apache 2.0").url("https://gitee.com/wells2333")))
				.externalDocs(new ExternalDocumentation().description(NAME).url("https://www.yunmianshi.com/"))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME).addList(TENANT_CODE))
				.components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
						new SecurityScheme().name(SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme("bearer")
								.bearerFormat("JWT")).addSecuritySchemes(TENANT_CODE,
						new SecurityScheme().name(TENANT_CODE).type(SecurityScheme.Type.APIKEY)
								.in(SecurityScheme.In.HEADER)));
	}
}
