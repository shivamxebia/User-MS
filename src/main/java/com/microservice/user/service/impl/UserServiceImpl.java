package com.microservice.user.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.microservice.user.dao.UserRepository;
import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserUpdateDto;
import com.microservice.user.response.UserPaginationResponse;
import com.microservice.user.service.UserService;
import com.microservice.user.util.ConstantUtil;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// Fetching all the users

	@Override
	public UserPaginationResponse getAllUsers(Integer pageNumber, Integer pageSize) {

		UserPaginationResponse response = new UserPaginationResponse();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Users> userPage = userRepository.findAll(pageable);
		response.setPageNo(pageNumber);
		response.setPageSize(pageSize);
		response.setPageCount(userPage.getTotalElements());
		response.setUserList(userPage.getContent());
		return response;
	}

	// Search User by Id

	@Override
	public Users getUserById(Long id) throws UserApplicationException {

		Optional<Users> userOptional = userRepository.findById(id);

		// If user does not exist
		if (!userOptional.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		return userOptional.get();
	}

	// Search user by username, phoneNumber, email and sending the response by
	// pagination.

	@Override
	public UserPaginationResponse searchUser(String username, String phoneNumber, String email, Integer pageNumber,
			Integer pageSize) throws UserApplicationException {

		UserPaginationResponse response = new UserPaginationResponse();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Users> userPage = userRepository.searchUser(username, email, phoneNumber, pageable);
		response.setPageNo(pageNumber);
		response.setPageSize(pageSize);
		response.setPageCount(userPage.getTotalElements());
		response.setUserList(userPage.getContent());
		return response;
	}

//  Updating User Profile in DB

	@Override
	public String updateUser(Long userId, UserUpdateDto userUpdateDto) throws UserApplicationException {
		Optional<Users> userOptional = userRepository.findById(userId);

		// If user does not exist
		if (!userOptional.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		// Updating user data
		Users user = userOptional.get();
		user.setFirstName(userUpdateDto.getFirstname());
		user.setLastName(userUpdateDto.getLastname());
		user.setPhoneNumber(userUpdateDto.getPhoneNumber());
		userRepository.save(user);
		return "User has been updated successfully";
	}

	@Override
	public String deleteUser(Long userId) throws UserApplicationException {

		Optional<Users> userOptional = userRepository.findById(userId);

		// If user does not exist
		if (!userOptional.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		// deleting user
		userRepository.deleteById(userId);
		return "User deleted Successfully";
	}

	@Override
	public String softDeleteUser(Long userId) throws UserApplicationException {

		Optional<Users> userOptional = userRepository.findById(userId);

		// If user does not exist in DB.
		if (!userOptional.isPresent()) {
			throw new UserApplicationException(HttpStatus.NOT_FOUND, ConstantUtil.USER_NOT_FOUND);
		}

		Users user = userOptional.get();
		user.setDeleted(true);
		userRepository.save(user);
		return "User is deleted!";
	}

}
