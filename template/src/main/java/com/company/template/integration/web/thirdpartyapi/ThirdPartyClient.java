package com.company.template.integration.web.thirdpartyapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.company.template.integration.web.thirdpartyapi.request.ThirdPartyApiRequest;
import com.company.template.integration.web.thirdpartyapi.response.ThirdPartyApiResponse;

@FeignClient(value = "third-party", url = "${third-party.api.base-url}")
public interface ThirdPartyClient {

	@PostMapping(path = "/send", consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ThirdPartyApiResponse sendSMS(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
			@RequestBody ThirdPartyApiRequest request) throws RuntimeException;

}
