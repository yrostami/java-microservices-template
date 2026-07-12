package com.company.template.exception;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.company.template.util.ResponseUtil;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	@Nullable
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();
		Locale locale = LocaleContextHolder.getLocale();

		Map<String, String> errors = bindingResult.getFieldErrors()
			.stream()
			.map((error) -> Map.entry(error.getField(), messageSource.getMessage(error, locale)))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		return ResponseEntity.badRequest().body(ResponseUtil.error(errors, HttpStatus.BAD_REQUEST.value()));
	}

	@ExceptionHandler(DuplicatedRequestException.class)
	public ResponseEntity<Object> handleDuplicatedRequestException(DuplicatedRequestException ex) {
		log.error("duplicate registration exception", ex);
		return new ResponseEntity<>(ResponseUtil.error(getLocalizedError(ex), HttpStatus.CONFLICT.value()),
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
		log.error("resource not found exception", ex);
		return new ResponseEntity<>(ResponseUtil.error(getLocalizedError(ex), HttpStatus.NOT_FOUND.value()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUnknownException(Exception ex) {
		log.error("unhandled exception", ex);
		MessageSourceResolvable error = () -> new String[] { "internal.server.error" };
		return new ResponseEntity<>(
				ResponseUtil.error(getLocalizedError(error), HttpStatus.INTERNAL_SERVER_ERROR.value()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private String getLocalizedError(MessageSourceResolvable msr) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(msr, locale);
	}

}
