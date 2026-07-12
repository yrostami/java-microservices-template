package com.company.template;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ImportAutoConfiguration(classes = { FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class })
@EnableFeignClients(basePackages = { "com.company.**.integration.web" })
@ExtendWith({ SpringExtension.class })
@TestPropertySource({ "classpath:application-test.properties", "classpath:third-party-api.properties" })
public abstract class FeignClientTestBase extends WireMockInitiator {

	@Configuration
	@EnableFeignClients
	static class TestConfig {

	}

}
