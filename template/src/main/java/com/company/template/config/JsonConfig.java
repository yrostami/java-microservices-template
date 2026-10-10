package com.company.template.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		JsonMapper jsonMapper = new JsonMapper();
		jsonMapper.registerModule(new JavaTimeModule());
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
		return jsonMapper;
	}

}
