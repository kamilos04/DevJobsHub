package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.model.Jwt;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.JwtRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.auth.ChangePasswordRequest;
import com.kamiljach.devjobshub.request.auth.LoginRequest;
import com.kamiljach.devjobshub.request.auth.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
import com.kamiljach.devjobshub.service.JwtService;
import com.kamiljach.devjobshub.service.UserService;
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

    private UserService userService;

    private JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest loginRequest) throws UserNotFoundByEmailException {
        String emailFromRequest = loginRequest.getEmail();
        String passwordFromRequest = loginRequest.getPassword();

        User user = userRepository.findByEmail(emailFromRequest).orElseThrow(UserNotFoundByEmailException::new);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), passwordFromRequest));

        String token = jwtConfig.createToken(user);

        jwtService.createJwt(token, user);

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
        newUser.setIsFirm(registerRequest.getIsFirm());
        newUser.setSurname(registerRequest.getSurname());

        userRepository.save(newUser);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getId(), registerRequest.getPassword()));

        String token = jwtConfig.createToken(newUser);

        jwtService.createJwt(token, newUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(newUser.getEmail());
        loginResponse.setToken(token);

        return loginResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse changePasswordByJwt(ChangePasswordRequest changePasswordRequest, String jwt){
        User user = userService.findUserByJwt(jwt);
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        jwtService.blockAllJwtFromUser(user);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), changePasswordRequest.getNewPassword()));

        String token = jwtConfig.createToken(user);

        jwtService.createJwt(token, user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setToken(token);

        return loginResponse;
    }
}
