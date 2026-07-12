package com.company.template.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Template Service")
			.version("1.0")
			.description("Template project for rapidly bootstrapping new microservices and applications."));
	}

	@Bean
	public GroupedOpenApi group1PublicApi() {
		return GroupedOpenApi.builder()
			.group("sample1-resources-public")
			.displayName("Sample1")
			.pathsToMatch("/v1/sample2/**")
			.build();
	}

	@Bean
	public GroupedOpenApi group2PublicApi() {
		return GroupedOpenApi.builder()
			.group("sample2-resources-public")
			.displayName("Sample2")
			.pathsToMatch("/v1/sample2/**")
			.build();
	}

}
