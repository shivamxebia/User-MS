package com.microservice.user.service;

import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.ChangePasswordDto;
import com.microservice.user.request.ResetPasswordDto;
import com.microservice.user.request.VerifyOtpDto;

public interface ResetPasswordService {
	
	String changePassword(ChangePasswordDto changePasswordDto) throws UserApplicationException;
	String generateOtp(String email) throws UserApplicationException;
	String verifyOtp(VerifyOtpDto verifyOtpDto) throws UserApplicationException;
	String changePasswordWithOtp(ResetPasswordDto newPassword) throws UserApplicationException;
}
