package com.microservice.user.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.user.dao.RegisterUserRepository;
import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserRegistrationRequestDto;
import com.microservice.user.request.createAccountRequestDto;
import com.microservice.user.service.RegistrationService;
import com.microservice.user.util.ConstantUtil;


@Service
public class RegistrationServiceImpl implements RegistrationService {

	private final RegisterUserRepository registerUserRepository;
	private final ModelMapper modelMapper;
	
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${onlineBanking.account.url}")
    private String accountServiceEndpointUrl;

	@Autowired
	public RegistrationServiceImpl(RegisterUserRepository registerUserRepository, ModelMapper modelMapper) {

		this.modelMapper = modelMapper;
		this.registerUserRepository = registerUserRepository;
	}

	@Override
	public String registerUser(UserRegistrationRequestDto userRegistrationRequestDto) throws UserApplicationException {
		
		//Converting the email to lowercase and trimming the leading and trailing spaces
		
		String email = userRegistrationRequestDto.getEmail().toLowerCase().trim();
		System.out.println("EMAIL : "+email);
		Optional<Users> optionalUser = registerUserRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			throw new UserApplicationException(HttpStatus.CONFLICT, ConstantUtil.USER_ALREADY_PRESENT);
		}
		
		userRegistrationRequestDto.setEmail(email);
		Users user = modelMapper.map(userRegistrationRequestDto, Users.class);
		registerUserRepository.save(user);
		
		String createAccountUrl = "http://localhost:8081/api/v1/account"; 
		System.out.println("URL : "+createAccountUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
     // Create request payload
        createAccountRequestDto requestPayload = new createAccountRequestDto();

        requestPayload.setUserId(user.getId());
        System.out.println("Request Payload id: " + requestPayload.getUserId());
        requestPayload.setAccountType(user.getAccountType());
        HttpEntity<createAccountRequestDto> requestEntity = new HttpEntity<>(requestPayload, headers);

        // Send POST request to the account service
        ResponseEntity<String> responseEntity =restTemplate.exchange(createAccountUrl, HttpMethod.POST, requestEntity, String.class);
        System.out.println("Request Payload: " + requestPayload);
        System.out.println("Response Body: " + responseEntity.getBody());
     // If response is not OK, handle the failure case
        if (responseEntity.getStatusCode() != HttpStatus.OK) {      	
        	throw new UserApplicationException((HttpStatus) responseEntity.getStatusCode(), "Failed to create account");
        } 
        
		
		
		return "User has been Created";
	}

	

}
