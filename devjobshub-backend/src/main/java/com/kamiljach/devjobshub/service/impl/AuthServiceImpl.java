package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsOnBlackListException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.BlackListJwt;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.BlackListJwtRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.auth.ChangePasswordRequest;
import com.kamiljach.devjobshub.request.auth.LoginRequest;
import com.kamiljach.devjobshub.request.auth.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.AuthService;
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

    private BlackListJwtRepository blackListJwtRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtConfig jwtConfig, PasswordEncoder passwordEncoder, UserService userService, BlackListJwtRepository blackListJwtRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtConfig = jwtConfig;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.blackListJwtRepository = blackListJwtRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest loginRequest) throws UserNotFoundByEmailException {
        String emailFromRequest = loginRequest.getEmail();
        String passwordFromRequest = loginRequest.getPassword();

        User user = userRepository.findByEmail(emailFromRequest).orElseThrow(UserNotFoundByEmailException::new);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), passwordFromRequest));

        String stringId = authentication.getName();
        User u = new User();
        u.setId(Long.parseLong(stringId));


        String token = jwtConfig.createToken(u);

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

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(newUser.getId(), registerRequest.getPassword()));

        String stringId = authentication.getName();
        User user = new User();
        user.setId(Long.parseLong(stringId));

        String token = jwtConfig.createToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(newUser.getEmail());
        loginResponse.setToken(token);

        return loginResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginResponse changePasswordByJwt(ChangePasswordRequest changePasswordRequest, String jwt) throws UserNotFoundByJwtException{
        User user = userService.findUserByJwt(jwt);
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), changePasswordRequest.getNewPassword()));

        BlackListJwt blackListJwt = new BlackListJwt();
        blackListJwt.setJwt(jwt);
        blackListJwtRepository.save(blackListJwt);

        String stringId = authentication.getName();
        User u = new User();
        u.setId(Long.parseLong(stringId));

        String token = jwtConfig.createToken(user);


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(user.getEmail());
        loginResponse.setToken(token);

        return loginResponse;
    }
}
