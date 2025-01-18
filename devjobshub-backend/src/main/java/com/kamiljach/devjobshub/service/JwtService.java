package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsBlockedException;
import com.kamiljach.devjobshub.model.User;

public interface JwtService {
    public void ifJwtIsBlockedThrowException(String jwt) throws JwtIsBlockedException;

    public void createJwt(String token, User user);

    public void blockAllJwtFromUser(User user);
}
