package com.company.template.config;

import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceConfigs {

	private final Environment env;

	public String getDummyEventTopic() {
		return env.getProperty("dummy-event.topic");
	}

}
