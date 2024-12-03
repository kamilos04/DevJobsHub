package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.login.LoginRequest;
import com.kamiljach.devjobshub.request.register.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest loginRequest){
        String emailFromRequest = loginRequest.getEmail();
        String passwordFromRequest = loginRequest.getPassword();
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

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse register(RegisterRequest registerRequest) throws AccountAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            throw new AccountAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setName(registerRequest.getName());
        newUser.setSurname(registerRequest.getSurname());
        newUser.setIsFirm(registerRequest.getIsFirm());

        userRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword()));

        String email = authentication.getName();
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        String token = jwtConfig.createToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(newUser.getEmail());
        loginResponse.setToken(token);

        return loginResponse;
    }
}
