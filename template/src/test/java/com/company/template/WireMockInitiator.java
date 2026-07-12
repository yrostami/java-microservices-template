package com.company.template;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.BindMode;
import org.testcontainers.junit.jupiter.Container;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Slf4j
public abstract class WireMockInitiator {

	@Container
	protected static WireMockContainer wiremockServer = new WireMockContainer(WireMockContainer.OFFICIAL_IMAGE_NAME)
		.withLogConsumer((outputFrame) -> log.debug(outputFrame.getUtf8String()))
		.withClasspathResourceMapping("wiremock/mappings/", "/home/wiremock/mappings/", BindMode.READ_ONLY);

	static {
		wiremockServer.start();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("third-party.api.base-url", wiremockServer::getBaseUrl);
	}

}
