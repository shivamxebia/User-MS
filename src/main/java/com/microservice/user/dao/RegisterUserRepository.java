package com.microservice.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.user.entity.Users;

@Repository
public interface RegisterUserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUsername(String username);

	Optional<Users> findByPhoneNumber(String phoneNumber);

	Optional<Users> findByEmail(String email);
}
