package com.company.template.spec.responce;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ControllerResponse<T> {

	private boolean successful;

	private String status;

	private T data;

	private Map<String, String> errors;

	private int code;

	private long timestamp;

	private String path;

	private String httpMethod;

	private String traceId;

}
