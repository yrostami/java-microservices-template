package com.company.template.exception;

import org.springframework.context.MessageSourceResolvable;

public class DuplicatedRequestException extends Exception implements MessageSourceResolvable {

	public DuplicatedRequestException(String message) {
		super(message);
	}

	@Override
	public String[] getCodes() {
		return new String[] { "duplicate.request" };
	}

}
