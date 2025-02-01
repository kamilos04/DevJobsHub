package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsBlockedException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.auth.ChangePasswordRequest;
import com.kamiljach.devjobshub.request.auth.LoginRequest;
import com.kamiljach.devjobshub.request.auth.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtConfig jwtConfig;

    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws UserNotFoundByEmailException {
        LoginResponse loginResponse = authService.login(loginRequest);
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) throws AccountAlreadyExistsException {
        LoginResponse loginResponse = authService.register(registerRequest);
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);

    }

    @PostMapping("/api/change-password")
    public ResponseEntity<LoginResponse> changePasswordByJwt(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException, JwtIsBlockedException {
        LoginResponse loginResponse = authService.changePasswordByJwt(changePasswordRequest, jwt);
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);

    }
}