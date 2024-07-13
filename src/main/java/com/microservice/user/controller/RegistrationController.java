package com.microservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserRegistrationRequestDto;
import com.microservice.user.service.RegistrationService;

import jakarta.validation.Valid;

@RequestMapping("/api/v1/")
@RestController
public class RegistrationController {

	private final RegistrationService registrationService;

	@Autowired
	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}
   
	// Registering a user in db
	
	@PostMapping("register")
	public ResponseEntity<String> registerUser(
			@Valid @RequestBody UserRegistrationRequestDto userRegistrationRequestDto)
			throws UserApplicationException {
		String response = registrationService.registerUser(userRegistrationRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
