package com.microservice.user.service;

import com.microservice.user.entity.Users;
import com.microservice.user.exception.UserApplicationException;
import com.microservice.user.request.UserUpdateDto;
import com.microservice.user.response.UserPaginationResponse;

public interface UserService {

	UserPaginationResponse getAllUsers(Integer pageNumber, Integer pageSize);
	
	Users getUserById(Long userId) throws UserApplicationException;

	UserPaginationResponse searchUser(String username, String phonenumber, String email, Integer pageNumber, Integer pageSize) throws UserApplicationException;

	String updateUser(Long id, UserUpdateDto userUpdateDto) throws UserApplicationException;

	String deleteUser(Long userId) throws UserApplicationException;

	String softDeleteUser(Long userId) throws UserApplicationException;

	String blockCard(Long userId,Long cardNumber,boolean isBlocked) throws UserApplicationException;

	String createTransaction(Long userId, Long amount, String transactionType) throws UserApplicationException;
}
