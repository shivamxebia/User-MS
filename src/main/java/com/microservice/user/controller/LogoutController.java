package com.microservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.service.LogoutService;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/")
@RestController
public class LogoutController {
	private final LogoutService logoutService;

	@Autowired
	public LogoutController(LogoutService logoutService) {
		this.logoutService = logoutService;
	}
    
	// To Logout a user
	@GetMapping("/logout")
	public ResponseEntity<Users> logout(@Valid @RequestParam Long id) throws UserApplicationException {
		Users response = logoutService.logoutUser(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
