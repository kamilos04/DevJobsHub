package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.response.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(String emailFromRequest, String passwordFromRequest);

    public LoginResponse register(String emailFromRequest, String passwordFromRequest, String name, String surname) throws AccountAlreadyExistsException;
}
