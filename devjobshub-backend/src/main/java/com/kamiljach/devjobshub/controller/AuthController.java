package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.errors.ApiError;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.login.LoginRequest;
import com.kamiljach.devjobshub.request.register.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse loginResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (BadCredentialsException ex){
        ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,"Invalid username or password", ex);
        return new ResponseEntity<ApiError>(errorResponse, errorResponse.getStatus());
        } catch (Exception ex){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,"Invalid username or password", ex);
            return new ResponseEntity<ApiError>(errorResponse, errorResponse.getStatus());
    }}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) throws AccountAlreadyExistsException {
        try {
            LoginResponse loginResponse = authService.register(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getName(), registerRequest.getSurname());
            return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        }catch (Exception ex){
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,"Something went wrong", ex);
            return new ResponseEntity<ApiError>(errorResponse, errorResponse.getStatus());
    }}
}
