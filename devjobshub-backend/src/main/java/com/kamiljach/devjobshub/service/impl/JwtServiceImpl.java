package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsBlockedException;
import com.kamiljach.devjobshub.model.Jwt;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.JwtRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private JwtRepository jwtRepository;

    private UserRepository userRepository;

    public JwtServiceImpl(JwtRepository jwtRepository, UserRepository userRepository) {
        this.jwtRepository = jwtRepository;
        this.userRepository = userRepository;
    }

    public void ifJwtIsBlockedThrowException(String jwt) throws JwtIsBlockedException {
        Optional<Jwt> jwtOptional = jwtRepository.findByJwt(jwt);
        if(jwtOptional.isEmpty()){
            return;
        }
        if(jwtOptional.get().getIsBlocked()){
            throw new JwtIsBlockedException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void createJwt(String token, User user){
        Jwt jwt = new Jwt();
        jwt.setJwt(token);
        jwt.putUser(user);
        jwtRepository.save(jwt);
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void blockAllJwtFromUser(User user){
        for(Jwt jwt : user.getJwtList()){
            jwt.setIsBlocked(true);
            jwtRepository.save(jwt);
        }
    }
}
