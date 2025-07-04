package com.nicat.rolebasedaccesscontrol.controller;

import com.nicat.rolebasedaccesscontrol.model.dto.request.EditProfileRequestDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.LoginRequestDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.RegisterRequestDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.EditProfileResponse;
import com.nicat.rolebasedaccesscontrol.model.dto.response.LoginResponse;
import com.nicat.rolebasedaccesscontrol.model.dto.response.ProfileResponse;
import com.nicat.rolebasedaccesscontrol.model.dto.response.RegisterResponseDto;
import com.nicat.rolebasedaccesscontrol.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public RegisterResponseDto register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        return userService.register(registerRequestDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @GetMapping("/show-profile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse showProfile() {
        return userService.showProfile();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(Authentication authentication, HttpServletRequest request,
                       HttpServletResponse response) {
        userService.logout(authentication, request,
                response);
    }
}