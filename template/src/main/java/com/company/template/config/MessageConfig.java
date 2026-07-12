package com.company.template.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class MessageConfig {

	private final Environment env;

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(env.getRequiredProperty("spring.messages.basename"));
		messageSource.setDefaultEncoding(env.getRequiredProperty("spring.messages.encoding"));
		messageSource.setUseCodeAsDefaultMessage(
				env.getRequiredProperty("spring.messages.use-code-as-default-message", Boolean.class));
		messageSource.setFallbackToSystemLocale(
				env.getRequiredProperty("spring.messages.fallback-to-system-locale", Boolean.class));
		return messageSource;
	}

}
