package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.errors.ApiError;
import com.kamiljach.devjobshub.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.login.LoginRequest;
import com.kamiljach.devjobshub.request.register.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtConfig jwtConfig;

    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            String email = authentication.getName();
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));

            String token = jwtConfig.createToken(user);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setEmail(user.getEmail());
            loginResponse.setToken(token);
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
        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            throw new AccountAlreadyExistsException();
        }
        try {
            User newUser = new User();
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setName(registerRequest.getName());
            newUser.setSurname(registerRequest.getSurname());

            userRepository.save(newUser);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

            String email = authentication.getName();
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            String token = jwtConfig.createToken(user);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setEmail(user.getEmail());
            loginResponse.setToken(token);
            return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        }catch (Exception ex){
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,"Something went wrong", ex);
            return new ResponseEntity<ApiError>(errorResponse, errorResponse.getStatus());
    }}
}
