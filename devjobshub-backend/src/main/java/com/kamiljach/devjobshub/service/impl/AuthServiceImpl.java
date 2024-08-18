package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtConfig jwtConfig;

    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtConfig jwtConfig, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(String emailFromRequest, String passwordFromRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailFromRequest, passwordFromRequest));
        String email = authentication.getName();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(passwordFromRequest));

        String token = jwtConfig.createToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setToken(token);
        return loginResponse;
    }
}
