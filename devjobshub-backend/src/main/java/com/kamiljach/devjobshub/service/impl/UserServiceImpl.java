package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.InvalidRequestException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.user.UpdateUserRequest;
import com.kamiljach.devjobshub.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private JwtConfig jwtConfig;
    private UserRepository userRepository;

    private OfferRepository offerRepository;


    public UserServiceImpl(JwtConfig jwtConfig, UserRepository userRepository, OfferRepository offerRepository) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    public User findUserByJwt(String jwt) {
        jwt = jwt.substring(7);
        Claims claims = jwtConfig.parseJwtClaims(jwt);
        String stringId = jwtConfig.getId(claims);

        return userRepository.findById(Long.parseLong(stringId)).get();
    }

    @Transactional
    public void addLikedOffer(User user, Offer offer){
        user.addLikedOffer(offer);
        userRepository.save(user);
        offerRepository.save(offer);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateUser(UpdateUserRequest updateUserRequest, String jwt) throws UserNotFoundByIdException, InvalidRequestException {
        User user = userRepository.findById(updateUserRequest.getId()).orElseThrow(UserNotFoundByIdException::new);

        user.setEmail(updateUserRequest.getEmail());
        user.setName(updateUserRequest.getName());
        user.setSurname(updateUserRequest.getSurname());

        userRepository.save(user);

    }



}
