package com.nicat.rolebasedaccesscontrol.service;

import com.nicat.rolebasedaccesscontrol.dao.entity.Role;
import com.nicat.rolebasedaccesscontrol.dao.entity.User;
import com.nicat.rolebasedaccesscontrol.dao.repository.RoleRepository;
import com.nicat.rolebasedaccesscontrol.dao.repository.TokenRepository;
import com.nicat.rolebasedaccesscontrol.dao.repository.UserRepository;
import com.nicat.rolebasedaccesscontrol.mapper.ProfileResponseMapper;
import com.nicat.rolebasedaccesscontrol.model.dto.request.LoginRequestDto;
import com.nicat.rolebasedaccesscontrol.model.dto.request.RegisterRequestDto;
import com.nicat.rolebasedaccesscontrol.model.dto.response.LoginResponse;
import com.nicat.rolebasedaccesscontrol.model.dto.response.ProfileResponse;
import com.nicat.rolebasedaccesscontrol.model.dto.response.RegisterResponseDto;
import com.nicat.rolebasedaccesscontrol.model.exception.NotFoundException;
import com.nicat.rolebasedaccesscontrol.model.exception.UnauthorizedException;
import com.nicat.rolebasedaccesscontrol.util.JwtUtil;
import com.nicat.rolebasedaccesscontrol.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtil;
    BCryptPasswordEncoder passwordEncoder;
    SecurityUtil securityUtil;
    ProfileResponseMapper profileResponseMapper;
    TokenRepository tokenRepository;

    public RegisterResponseDto register(@Valid RegisterRequestDto registerRequestDto) {
        log.info("register method was started for userService");
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("This role was not found"));
        log.info("default user role was found");

        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent())
            throw new RuntimeException("This username is available");
        log.info("this username is not available so that username can be used");
        User user = User.builder()
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .roles(Set.of(userRole))
                .isEnable(true)
                .build();
        log.info("dto was successfully set to user object");
        userRepository.save(user);
        log.info("user was successfully saved in database");
        RegisterResponseDto registerResponseDto = new RegisterResponseDto();
        log.info("registerResponseDto was successfully created");
        registerResponseDto.setUsername(user.getUsername());
        log.info("username was successfully set to registerResponseDto");
        return registerResponseDto;
    }

    public LoginResponse login(@Valid LoginRequestDto loginRequestDto) {
        log.info("login method was started for UserService");
        User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new RuntimeException("This user not found with this username: " + loginRequestDto.getUsername()));
        log.info("user was found");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken
                            (loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Invalid username or password");
        }
        log.info("authentication manager with successfully authenticate for this username: {}", loginRequestDto.getUsername());
        var accessToken = jwtUtil.generateAccessToken(user);
        log.info("access token created");
        var refreshToken = jwtUtil.generateRefreshToken(user);
        log.info("refresh token created");
        jwtUtil.revokeAllTokensOfUser(user);
        jwtUtil.saveUserToken(user, accessToken, refreshToken);

        LoginResponse LoginResponse = new LoginResponse();
        LoginResponse.setAccessToken(accessToken);
        LoginResponse.setRefreshToken(refreshToken);
        log.info("User successfully logged in");
        return LoginResponse;
    }

    public ProfileResponse showProfile() {
        log.info("showProfile method was started for UserService");
        String username = securityUtil.getCurrentUsername();
        if (username == null || username.isEmpty()) {
            log.error("Username is null or empty");
            throw new NotFoundException("Username is null or empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("This user not found"));
        return profileResponseMapper.toProfileResponse(user);
    }

    public void logout(Authentication authentication,
                       HttpServletRequest request, HttpServletResponse response) {
        log.info("logout method was started for UserService");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("OOPS,something is wrong");
            throw new UnauthorizedException("Please register without any problems");
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByAccessToken(jwt).orElse(null);
        if (storedToken != null) {
            log.info("stored token is not null");
            storedToken.setIsLoggedOut(Boolean.TRUE);
            log.info("stored token was logged out and ");
            tokenRepository.save(storedToken);
            log.info("token saved like TRUE (isLoggedOut field) in database");
            SecurityContextHolder.clearContext();
            log.info("clear security context holder with successfully");
        }
    }
}