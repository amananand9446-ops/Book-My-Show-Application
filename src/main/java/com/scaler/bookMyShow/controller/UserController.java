
package com.scaler.bookMyShow.controller;

import com.scaler.bookMyShow.dto.LoginRequestDto;
import com.scaler.bookMyShow.dto.LoginResponseDto;
import com.scaler.bookMyShow.dto.UserRequestDto;
import com.scaler.bookMyShow.dto.UserResponseDto;
import com.scaler.bookMyShow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerUser(
            @RequestBody UserRequestDto request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(
            @RequestBody LoginRequestDto request) {
        return userService.login(request);
    }
}
