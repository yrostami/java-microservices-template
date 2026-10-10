package com.company.template.controller.interceptor;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import com.company.template.util.Constants;

@Slf4j
public class RequestContextInterceptor implements HandlerInterceptor {

	private static final String REQUEST_CONTEXT_NAME = "current-request-context";

	private static final String UNKNOWN_TRACE_ID = "unknown";

	public static RequestContext getCurrentContext() {
		return (RequestContext) RequestContextHolder.currentRequestAttributes()
			.getAttribute(REQUEST_CONTEXT_NAME, RequestAttributes.SCOPE_REQUEST);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		RequestContext requestContext = new RequestContext();
		requestContext.setUserId(request.getHeader(Constants.REQUEST_HEADER_USER_ID));
		requestContext.setMethod(request.getMethod());
		requestContext.setPath(request.getRequestURI());
		requestContext.setQuery(request.getQueryString());
		requestContext.setTraceId(getTraceId());
		RequestContextHolder.currentRequestAttributes()
			.setAttribute(REQUEST_CONTEXT_NAME, requestContext, RequestAttributes.SCOPE_REQUEST);

		return true;
	}

	private String getTraceId() {
		try {
			Span currentSpan = Span.current();
			if (!currentSpan.isRecording()) {
				log.warn("No active recording span found");
				return UNKNOWN_TRACE_ID;
			}
			SpanContext context = currentSpan.getSpanContext();
			if (!context.isValid()) {
				log.warn("Invalid span context detected");
				return UNKNOWN_TRACE_ID;
			}
			return context.getTraceId();
		}
		catch (Exception ex) {
			log.error("Error retrieving trace ID", ex);
			return UNKNOWN_TRACE_ID;
		}
	}

	@Setter
	@Getter
	@ToString
	public static class RequestContext {

		private String method;

		private String path;

		private String query;

		private String userId;

		private String traceId;

	}

}