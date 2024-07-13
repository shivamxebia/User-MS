package com.onlineBanking.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.onlineBanking.user.dao.RegisterUserRepository;
import com.onlineBanking.user.entity.Users;
import com.onlineBanking.user.exception.UserApplicationException;
import com.onlineBanking.user.request.UserLoginRequestDto;
import com.onlineBanking.user.service.impl.LoginServiceImpl;
import com.onlineBanking.user.util.ConstantUtil;

public class LoginServiceImplTest {

    @Mock
    private RegisterUserRepository userRepository;

    @InjectMocks
    private LoginServiceImpl loginService;
    
	private Users user;
	private UserLoginRequestDto userLoginRequestDto;

    @BeforeEach
    public void setUp() {
		user = new Users();
		user.setEmail("test@test.com");
		user.setPassword("password");
		user.setNumberOfAttempts(0);
		user.setBlocked(false);
		user.setLoggedIn(false);

		userLoginRequestDto = new UserLoginRequestDto();
		userLoginRequestDto.setEmail("test@test.com");
		userLoginRequestDto.setPassword("password");
    }

    @Test
    public void testLoginUser_UserNotFound() {
        assertThrows(UserApplicationException.class, () -> loginService.loginUser(userLoginRequestDto));
        verify(userRepository).findByEmail(eq("nonexistent@example.com"));
    }

    @Test
    public void testLoginUser_AccountBlocked() {
        // Arrange
        Users blockedUser = new Users();
        blockedUser.setEmail("blocked@example.com");
        blockedUser.setPassword("password");
        blockedUser.setBlocked(true);
        blockedUser.setBlockedDate(LocalDateTime.now().minusHours(1));
        blockedUser.setNumberOfAttempts(5);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(blockedUser));


        // Act & Assert
        assertThrows(UserApplicationException.class, () -> loginService.loginUser(userLoginRequestDto));
        verify(userRepository).save(blockedUser);
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        // Arrange
        Users validUser = new Users();
        validUser.setEmail("valid@example.com");
        validUser.setPassword("password");
        validUser.setNumberOfAttempts(4);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));

//        UserLoginRequestDto requestDto = new UserLoginRequestDto("valid@example.com", "wrongpassword");

        // Act & Assert
        assertThrows(UserApplicationException.class, () -> loginService.loginUser(userLoginRequestDto));
        assertEquals(1, validUser.getNumberOfAttempts());
        verify(userRepository).save(validUser);
    }
//
    @Test
    public void testLoginUser_SuccessfulLogin() throws UserApplicationException {
        // Arrange
        Users validUser = new Users();
        validUser.setEmail("valid@example.com");
        validUser.setPassword("password");
        validUser.setNumberOfAttempts(0);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));

//        UserLoginRequestDto requestDto = new UserLoginRequestDto("valid@example.com", "password");

        // Act
        Users loggedInUser = loginService.loginUser(userLoginRequestDto);

        // Assert
        assertTrue(loggedInUser.isLoggedIn());
        assertEquals(0, validUser.getNumberOfAttempts());
        verify(userRepository).save(validUser);
    }
}
