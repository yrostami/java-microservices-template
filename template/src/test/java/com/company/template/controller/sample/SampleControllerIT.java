package com.company.template.controller.sample;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.company.template.BaseIntegrationTest;
import com.company.template.entity.sample.SampleEntity;
import com.company.template.entity.sample.SampleEntityStatus;
import com.company.template.repository.SampleRepository;
import com.company.template.spec.request.SampleRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SampleControllerIT extends BaseIntegrationTest {

	@Autowired
	private SampleRepository sampleRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getSample_validTrackingCode_successful() throws Exception {
		String trackingCode = "test_sample_trc_1";
		String name = "test-sample-name";
		SampleEntity sample = createNewSample(name, trackingCode);
		sampleRepository.save(sample);

		mockMvc.perform(get("/v1/sample/{trackingCode}", trackingCode).accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.name", equalTo(name)))
			.andExpect(jsonPath("$.data.status", equalTo(2)))
			.andExpect(jsonPath("$.data.trackingCode", equalTo(trackingCode)))
			.andExpect(jsonPath("$.successful", equalTo(true)))
			.andExpect(jsonPath("$.status", equalTo("successful")))
			.andExpect(jsonPath("$.code", equalTo(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
			.andExpect(jsonPath("$.path", equalTo("/v1/sample/" + trackingCode)))
			.andExpect(jsonPath("$.httpMethod", equalTo("GET")))
			.andExpect(jsonPath("$.traceId", notNullValue()))
			.andExpect(jsonPath("$.traceId", not(equalTo("unknown"))));
	}

	@Test
	public void getSample_invalidTrackingCode_failed() throws Exception {
		String trackingCode = "invalid_test_sample_trc_1";

		mockMvc.perform(get("/v1/sample/{trackingCode}", trackingCode).accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.successful", equalTo(false)))
			.andExpect(jsonPath("$.status", equalTo("error")))
			.andExpect(jsonPath("$.code", equalTo(404)))
			.andExpect(jsonPath("$.errors", equalTo(Map.of("1", "resource not found."))))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
			.andExpect(jsonPath("$.path", equalTo("/v1/sample/" + trackingCode)))
			.andExpect(jsonPath("$.httpMethod", equalTo("GET")))
			.andExpect(jsonPath("$.traceId", notNullValue()))
			.andExpect(jsonPath("$.traceId", not(equalTo("unknown"))));
	}

	@Test
	public void createSample_successful() throws Exception {
		String name = "test-sample-name-1";
		SampleRequest request = new SampleRequest();
		request.setName(name);

		mockMvc
			.perform(post("/v1/sample").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsBytes(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.successful", equalTo(true)))
			.andExpect(jsonPath("$.data.name", equalTo(name)))
			.andExpect(jsonPath("$.data.status", equalTo(0)))
			.andExpect(jsonPath("$.data.trackingCode", notNullValue()))
			.andExpect(jsonPath("$.status", equalTo("successful")))
			.andExpect(jsonPath("$.code", equalTo(0)))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
			.andExpect(jsonPath("$.path", equalTo("/v1/sample")))
			.andExpect(jsonPath("$.httpMethod", equalTo("POST")))
			.andExpect(jsonPath("$.traceId", notNullValue()))
			.andExpect(jsonPath("$.traceId", not(equalTo("unknown"))));
	}

	@Test
	public void createSample_duplicated_returns409() throws Exception {
		String name = "test-sample-name-2";
		SampleRequest request = new SampleRequest();
		request.setName(name);
		byte[] requestBody = objectMapper.writeValueAsBytes(request);

		mockMvc
			.perform(post("/v1/sample").accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody))
			.andExpect(status().isCreated());

		mockMvc
			.perform(post("/v1/sample").accept(MediaType.APPLICATION_JSON_VALUE)
				.header("Accept-Language", "fa")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.successful", equalTo(false)))
			.andExpect(jsonPath("$.status", equalTo("error")))
			.andExpect(jsonPath("$.code", equalTo(409)))
			.andExpect(jsonPath("$.errors", equalTo(Map.of("1", "درخواست تکراری است"))))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
			.andExpect(jsonPath("$.path", equalTo("/v1/sample")))
			.andExpect(jsonPath("$.httpMethod", equalTo("POST")))
			.andExpect(jsonPath("$.traceId", notNullValue()));
	}

	@Test
	public void createSample_invalidRequest_returns400() throws Exception {
		SampleRequest request = new SampleRequest();
		byte[] requestBody = objectMapper.writeValueAsBytes(request);

		mockMvc
			.perform(post("/v1/sample").accept(MediaType.APPLICATION_JSON_VALUE)
				.header("Accept-Language", "fa")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.successful", equalTo(false)))
			.andExpect(jsonPath("$.status", equalTo("error")))
			.andExpect(jsonPath("$.code", equalTo(400)))
			.andExpect(jsonPath("$.errors", equalTo(Map.of("name", "مقدار نباید خالی باشد"))))
			.andExpect(jsonPath("$.timestamp", notNullValue()))
			.andExpect(jsonPath("$.path", equalTo("/v1/sample")))
			.andExpect(jsonPath("$.httpMethod", equalTo("POST")))
			.andExpect(jsonPath("$.traceId", notNullValue()));
	}

	private SampleEntity createNewSample(String name, String trackingCode) {
		SampleEntity sample = new SampleEntity();
		sample.setName(name);
		sample.setTrackingCode(trackingCode);
		sample.setStatus(SampleEntityStatus.COMPLETED);
		return sample;
	}

}
