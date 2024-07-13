package com.microservice.user.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.RegisterUserRepository;
import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.service.LogoutService;
import com.microservice.user.util.ConstantUtil;

@Service
public class LogoutServiceImpl implements LogoutService {
	private final RegisterUserRepository registerUserRepository;

	@Autowired
	public LogoutServiceImpl(RegisterUserRepository registerUserRepository) {
		this.registerUserRepository = registerUserRepository;
	}

	@Override
	public Users logoutUser(Long id) throws UserApplicationException {
		
		Optional<Users> optionalUser = registerUserRepository.findById(id);
		
		//If User not present in DB. 
		if (!optionalUser.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}
          
		Users user = optionalUser.get();
		user.setLoggedIn(false);
		registerUserRepository.save(user);
		return user;
	}

}
