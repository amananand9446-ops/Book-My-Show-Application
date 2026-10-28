
package com.scaler.bookMyShow.service;

import com.scaler.bookMyShow.Repository.UserRepository;
import com.scaler.bookMyShow.dto.LoginRequestDto;
import com.scaler.bookMyShow.dto.LoginResponseDto;
import com.scaler.bookMyShow.dto.UserRequestDto;
import com.scaler.bookMyShow.dto.UserResponseDto;
import com.scaler.bookMyShow.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserShouldHashPassword() {
        UserRequestDto request = new UserRequestDto();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("Password123"))
                .thenReturn("$2a$12$fakeHashedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1);
                    return user;
                });

        UserResponseDto response = userService.registerUser(request);

        assertEquals(1, response.getId());
        assertEquals("test@example.com", response.getEmail());
        verify(passwordEncoder).encode("Password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserShouldRejectDuplicateEmail() {
        UserRequestDto request = new UserRequestDto();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("Password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginShouldReturnTokenForCorrectPassword() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("stored-hash");

        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("Password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123", "stored-hash"))
                .thenReturn(true);
        when(jwtService.generateToken("test@example.com"))
                .thenReturn("test-jwt-token");

        LoginResponseDto response = userService.login(request);

        assertEquals("test-jwt-token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
    }

    @Test
    void loginShouldRejectIncorrectPassword() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("stored-hash");

        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("WrongPassword");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPassword", "stored-hash"))
                .thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.login(request)
        );

        verify(jwtService, never()).generateToken(anyString());
    }
}
