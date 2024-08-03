package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.errors.ApiError;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.request.login.LoginRequest;
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

@Controller
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private JwtConfig jwtConfig;

    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
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
        } catch (BadCredentialsException e){
        ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,"Invalid username or password", e);
        return new ResponseEntity<ApiError>(errorResponse, errorResponse.getStatus());
        } catch (Exception e){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,"Invalid username or password", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }}

    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){

    }
}
