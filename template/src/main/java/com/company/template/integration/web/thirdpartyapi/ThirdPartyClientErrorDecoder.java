package com.company.template.integration.web.thirdpartyapi;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

@Slf4j
public class ThirdPartyClientErrorDecoder implements ErrorDecoder {

	private final ObjectMapper objectMapper;

	public ThirdPartyClientErrorDecoder() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		String body = readBodyAsString(response);
		log.error("third-party API ({}) call Error: {}", methodKey, body);
		if (List.of(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.REQUEST_TIMEOUT, HttpStatus.BAD_GATEWAY)
			.contains(response.status())) {
			String errorMsg = String.format("connection error in %s, http status:%s", methodKey, response.status());
			log.error(errorMsg);
			return new ConnectException(errorMsg);
		}

		// Todo; check other statuses and the response then throw relevant Exception

		return new Exception();
	}

	private String readBodyAsString(Response response) {
		try {
			Reader reader = response.body().asReader(StandardCharsets.UTF_8);
			try (StringWriter writer = new StringWriter()) {
				reader.transferTo(writer);
				return writer.toString();
			}
		}
		catch (IOException ex) {
			log.error("error in reading API error response body: {} ", response.body(), ex);
			return "unable to read response body";
		}
	}

}
