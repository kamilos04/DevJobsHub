package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;

public interface UserService {
    public User findUserByJwt(String jwt) throws UserNotFoundByJwtException;
}
