package com.microservice.user.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findById(Long id);

	Optional<Users> findByUsername(String username);

	Optional<Users> findByPhoneNumber(String phoneNumber);

	Optional<Users> findByEmail(String email);

	@Query("select users from Users users where (:username is null or users.username like %:username%) and (:email is null or users.email like %:email%) and (:phoneNumber is null or users.phoneNumber like %:phoneNumber%) ")
	Page<Users> searchUser(@Param("username") String username, @Param("email") String email,
			@Param("phoneNumber") String phoneNumber, Pageable pageable);
	
}
