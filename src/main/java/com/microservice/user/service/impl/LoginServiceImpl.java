package com.microservice.user.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.RegisterUserRepository;
import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserLoginRequestDto;
import com.microservice.user.service.LoginService;
import com.microservice.user.util.ConstantUtil;

@Service
public class LoginServiceImpl implements LoginService {

	private static final int MAX_ATTEMPTS = 5;
	private static final int BLOCK_DURATION_HOURS = 24;

	private final RegisterUserRepository registerUserRepository;

	@Autowired
	public LoginServiceImpl(RegisterUserRepository registerUserRepository) {
		this.registerUserRepository = registerUserRepository;
	}

	@Override
	public Users loginUser(UserLoginRequestDto userLoginRequestDto) throws UserApplicationException {
         
		Optional<Users> optionalUser = registerUserRepository.findByEmail(userLoginRequestDto.getEmail().toLowerCase().trim());
		
		// If User does not exist
		if (!optionalUser.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		Users user = optionalUser.get();
        
		// If User is blocked
		
		if (user.isBlocked()) {
			if (!user.getBlockedDate().plusHours(BLOCK_DURATION_HOURS).isBefore(LocalDateTime.now())) {
				throw new UserApplicationException(HttpStatus.FORBIDDEN, ConstantUtil.ACCOUNT_BLOCKED);

			}
			user.setBlocked(false);
			user.setNumberOfAttempts(0);
			user.setBlockedDate(null);
		}
		if (!userLoginRequestDto.getPassword().equals(user.getPassword())) {

			user.setNumberOfAttempts(user.getNumberOfAttempts() + 1);
			if (user.getNumberOfAttempts() >= MAX_ATTEMPTS) {
				user.setBlocked(true);
				user.setBlockedDate(LocalDateTime.now());
			}
			registerUserRepository.save(user);
			throw new UserApplicationException(HttpStatus.UNAUTHORIZED, ConstantUtil.INVALID_CREDENTIALS);

		}
		user.setNumberOfAttempts(0);
		user.setLoggedIn(true);
		registerUserRepository.save(user);
		return user;
	}

}
