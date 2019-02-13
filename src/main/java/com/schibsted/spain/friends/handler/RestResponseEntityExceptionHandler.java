package com.schibsted.spain.friends.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	// ya fue aceptada un 409
	// requests de estado

	// Crear una excepcion diferente para not founds
	// Crear otra excepcion no uses illegal argument
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