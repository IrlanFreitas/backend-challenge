package com.schibsted.spain.friends.handler;

import com.schibsted.spain.friends.exceptions.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {BadRequestException.class})
	protected ResponseEntity<Object> returnBadRequest(RuntimeException ex, WebRequest request) {
		return handleExceptionInternal(
				ex,
				ex.toString(),
				new HttpHeaders(),
				HttpStatus.BAD_REQUEST,
				request
		);
	}

	@ExceptionHandler(value = {IllegalArgumentException.class})
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		return handleExceptionInternal(
				ex,
				ex.toString(),
				new HttpHeaders(),
				HttpStatus.BAD_REQUEST,
				request
		);
	}
}