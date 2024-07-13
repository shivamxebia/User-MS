package com.microservice.user.exception;

import org.springframework.http.HttpStatus;

public class UserApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1149579499236856530L;

	private HttpStatus httpStatus;
	private String message;

	
	public UserApplicationException(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
	}
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
