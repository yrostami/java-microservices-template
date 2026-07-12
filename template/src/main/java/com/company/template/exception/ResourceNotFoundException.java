package com.company.template.exception;

import org.springframework.context.MessageSourceResolvable;

public class ResourceNotFoundException extends Exception implements MessageSourceResolvable {

	public ResourceNotFoundException(String msg) {
		super(msg);
	}

	public static ResourceNotFoundException from(Class clazz, String trackingCode) {
		String resourceName = (clazz != null) ? clazz.getSimpleName() : "resource";
		String msg = String.format("no %s found with trackingCode %s", resourceName, trackingCode);
		return new ResourceNotFoundException(msg);
	}

	@Override
	public String[] getCodes() {
		return new String[] { "resource.not.found" };
	}

}
