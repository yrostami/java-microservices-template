package com.company.template.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

	private final ServiceConfigs configs;

	@Bean
	public NewTopic dummyEventTopic() {
		return TopicBuilder.name(configs.getDummyEventTopic()).partitions(64).build();
	}

}