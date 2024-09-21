package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional(rollbackOn = Exception.class)
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

    @Transactional(rollbackOn = Exception.class)
    public LoginResponse register(String emailFromRequest, String passwordFromRequest, String name, String surname) throws AccountAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(emailFromRequest);
        if(optionalUser.isPresent()){
            throw new AccountAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setEmail(emailFromRequest);
        newUser.setPassword(passwordEncoder.encode(passwordFromRequest));
        newUser.setName(name);
        newUser.setSurname(surname);

        userRepository.save(newUser);

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
