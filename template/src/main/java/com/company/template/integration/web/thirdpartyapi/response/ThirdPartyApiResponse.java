package com.company.template.integration.web.thirdpartyapi.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ThirdPartyApiResponse {

	private String message;

	private List<Integer> result;

}
