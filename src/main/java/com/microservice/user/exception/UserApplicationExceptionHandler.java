package com.microservice.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserApplicationExceptionHandler {
	@ExceptionHandler(value = { UserApplicationException.class })
	public ResponseEntity<Object> handleUserApplicationException(UserApplicationException userApplicationException) {
		return ResponseEntity.status(userApplicationException.getHttpStatus())
				.body(userApplicationException.getMessage());
	}
}
