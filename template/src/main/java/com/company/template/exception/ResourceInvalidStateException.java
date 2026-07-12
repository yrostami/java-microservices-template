package com.company.template.exception;

import org.springframework.context.MessageSourceResolvable;

public class ResourceInvalidStateException extends Exception implements MessageSourceResolvable {

	public ResourceInvalidStateException(String msg) {
		super(msg);
	}

	@Override
	public String[] getCodes() {
		return new String[] { "resource.invalid.state" };
	}

}
