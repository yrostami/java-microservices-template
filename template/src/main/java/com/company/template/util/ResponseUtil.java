package com.company.template.util;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.template.controller.interceptor.RequestContextInterceptor;
import com.company.template.controller.interceptor.RequestContextInterceptor.RequestContext;
import com.company.template.spec.responce.ControllerResponse;

public final class ResponseUtil {

	private static final String SUCCESSFUL_STATUS = "successful";

	private static final String FAILURE_STATUS = "error";

	private ResponseUtil() {
	}

	public static ControllerResponse<Void> successful() {
		return successful(null);
	}

	public static <T> ControllerResponse<T> successful(T data) {
		ControllerResponse<T> response = new ControllerResponse<>();
		response.setSuccessful(true);
		response.setStatus(SUCCESSFUL_STATUS);
		response.setData(data);
		response.setErrors(null);
		response.setCode(0);
		response.setTimestamp(Instant.now().toEpochMilli());
		setRequestDate(response);
		return response;
	}

	public static <T> ControllerResponse<T> error(Map<String, String> errors, int errorCode) {
		ControllerResponse<T> response = new ControllerResponse<>();
		response.setSuccessful(false);
		response.setStatus(FAILURE_STATUS);
		response.setData(null);
		response.setErrors(errors);
		response.setCode(errorCode);
		response.setTimestamp(Instant.now().toEpochMilli());
		setRequestDate(response);
		return response;
	}

	public static <T> ControllerResponse<T> error(List<String> errors, int errorCode) {
		Map<String, String> errorsMap = new HashMap<>();
		for (int i = 1; i <= errors.size(); i++) {
			errorsMap.put(String.valueOf(i), errors.get(i - 1));
		}
		return error(errorsMap, errorCode);
	}

	public static <T> ControllerResponse<T> error(String error, int errorCode) {
		return error(Map.of("1", error), errorCode);
	}

	private static void setRequestDate(ControllerResponse<?> response) {
		RequestContext requestContext = RequestContextInterceptor.getCurrentContext();
		String path = requestContext.getPath();
		if (requestContext.getQuery() != null) {
			path += "?" + requestContext.getQuery();
		}
		response.setPath(path);
		response.setHttpMethod(requestContext.getMethod());
		response.setTraceId(requestContext.getTraceId());
	}

}