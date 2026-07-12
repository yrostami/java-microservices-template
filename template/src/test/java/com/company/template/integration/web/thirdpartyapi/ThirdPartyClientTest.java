package com.company.template.integration.web.thirdpartyapi;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import com.company.template.FeignClientTestBase;
import com.company.template.integration.web.thirdpartyapi.request.ThirdPartyApiRequest;
import com.company.template.integration.web.thirdpartyapi.response.ThirdPartyApiResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

class ThirdPartyClientTest extends FeignClientTestBase {

	private static final String AUTH_TOKEN = "Bearer test-token";

	private static final String RECIPIENT = "09123456789";

	private static final String SENDER = "500030005711";

	private static final String MESSAGE = "Test message";

	private static final String USER_TAG = "test-tag";

	@Autowired
	private ThirdPartyClient thirdPartyClient;

	@Test
	void sendSMS_ShouldReturnSuccessResponse() {
		ThirdPartyApiRequest request = new ThirdPartyApiRequest();
		request.setMessageBodies(List.of(MESSAGE));
		request.setRecipientNumbers(List.of(RECIPIENT));
		request.setSenderNumber(SENDER);
		request.setUserTag(USER_TAG);

		ThirdPartyApiResponse response = thirdPartyClient.sendSMS(AUTH_TOKEN, request);

		assertThat(response).isNotNull();
		assertThat(response.getMessage()).isEqualTo("success");
		assertThat(response.getResult()).isNotEmpty();
		assertThat(response.getResult().get(0)).isEqualTo(1); // Assuming 1 means success
																// in the API
	}

	@Test
	void sendSMS_ShouldHandleErrorResponse() {
		ThirdPartyApiRequest request = new ThirdPartyApiRequest();
		request.setMessageBodies(List.of(MESSAGE));
		request.setRecipientNumbers(List.of("invalid-number"));
		request.setSenderNumber(SENDER);
		request.setUserTag(USER_TAG);

		Exception actual = assertThatException().isThrownBy(() -> thirdPartyClient.sendSMS(AUTH_TOKEN, request))
			.isInstanceOf(Exception.class)
			.actual();
		// Todo: check the actual exception with excepted
	}

}
