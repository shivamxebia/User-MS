package com.microservice.user.request;

import jakarta.validation.constraints.NotEmpty;

public class VerifyOtpDto {
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	private String otp;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
}
