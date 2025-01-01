package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.JwtConfig;
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
import org.springframework.stereotype.Service;

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
    public User findUserByJwt(String jwt) throws UserNotFoundByJwtException {
        jwt = jwt.substring(7);
        Claims claims = jwtConfig.parseJwtClaims(jwt);
        String stringId = jwtConfig.getId(claims);
        Optional<User> optionalUser = userRepository.findById(Long.parseLong(stringId));
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        else{
            throw new UserNotFoundByJwtException();
        }
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


        if (!user.getIsFirm() && updateUserRequest.getSurname()!= null){
            verifySurnameOrThrowException(updateUserRequest.getSurname());
            user.setSurname(updateUserRequest.getSurname());
        }

        userRepository.save(user);

    }

    public void verifySurnameOrThrowException(String surname) throws InvalidRequestException {
        if(surname.isBlank() || surname.length()>100){
            throw new InvalidRequestException("surname is invalid");
        }
    }


}
