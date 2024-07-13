package com.microservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserLoginRequestDto;
import com.microservice.user.service.LoginService;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/")
@RestController
public class LoginController {

	private final LoginService loginService;

	@Autowired
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
    
	//To Login a User
	@PostMapping("/login")
	public ResponseEntity<Users> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginDto)
			throws UserApplicationException {
		Users response = loginService.loginUser(userLoginDto);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
}