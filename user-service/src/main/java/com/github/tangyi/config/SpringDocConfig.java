package com.github.tangyi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tangyi
 * @date 2022/4/8 8:16 下午
 */
@Configuration
public class SpringDocConfig {

	private static final String SECURITY_SCHEME_NAME = "BearerAuth";

	public static final String NAME = "云面试";

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title(NAME).description(NAME).version("v1.0.0")
						.license(new License().name("Apache 2.0").url("https://gitee.com/wells2333")))
				.externalDocs(new ExternalDocumentation().description(NAME).url("https://www.yunmianshi.com/"))
				.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)).components(
						new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
								new SecurityScheme().name(SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP)
										.scheme("bearer").bearerFormat("JWT")));
	}

	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder().group("user").pathsToMatch("/user-service/**").build();
	}

	@Bean
	public GroupedOpenApi examApi() {
		return GroupedOpenApi.builder().group("exam").pathsToMatch("/exam-service/**").build();
	}
}
