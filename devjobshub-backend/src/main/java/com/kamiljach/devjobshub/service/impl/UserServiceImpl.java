package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private JwtConfig jwtConfig;
    private UserRepository userRepository;


    public UserServiceImpl(JwtConfig jwtConfig, UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByJwt(String jwt) throws UserNotFoundByJwtException {
        Claims claims = jwtConfig.parseJwtClaims(jwt);
        String email = jwtConfig.getEmail(claims);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        else{
            throw new UserNotFoundByJwtException();
        }
    }
    @Override
    public User getProfileByJwt(String jwt) throws UserNotFoundByJwtException {
        return findUserByJwt(jwt);
    }
}
