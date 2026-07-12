package com.company.template.integration.web.thirdpartyapi.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ThirdPartyApiRequest {

	private List<String> messageBodies;

	private List<String> recipientNumbers;

	private String senderNumber;

	private String userTag;

}
