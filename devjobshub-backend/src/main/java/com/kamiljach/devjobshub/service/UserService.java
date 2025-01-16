package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.InvalidRequestException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.request.user.UpdateUserRequest;

public interface UserService {
    public User findUserByJwt(String jwt);
    public void addLikedOffer(User user, Offer offer);

    public void updateUser(UpdateUserRequest updateUserRequest, String jwt) throws UserNotFoundByIdException, InvalidRequestException;
}
