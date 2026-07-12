package com.company.template.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.company.template.util.IdGenerator;
import com.company.template.util.Snowflake;

@Configuration
@RequiredArgsConstructor
public class SnowflakeConfig {

	private final Environment environment;

    @Value("${spring.application.name}")
	private String HOSTNAME;

	@Bean
	public IdGenerator snowflake() {
		long nodeId;
		String host = environment.getProperty(HOSTNAME, "");
		if (host.isEmpty()) {
			return new Snowflake();
		}
		else {
			nodeId = hashTo10Bits(host);
		}
		return new Snowflake(nodeId);
	}

	private static long hashTo10Bits(String s) {
		// Simple, fast, and consistent across restarts if HOSTNAME is stable
		long h = s.hashCode(); // 32-bit hash
		return h & Snowflake.MAX_NODE_ID; // keep lower 10 bits
	}

}
