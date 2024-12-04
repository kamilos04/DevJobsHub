package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.request.login.LoginRequest;
import com.kamiljach.devjobshub.request.register.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(LoginRequest loginRequest) throws UserNotFoundByEmailException;

    public LoginResponse register(RegisterRequest registerRequest) throws AccountAlreadyExistsException;
}
