package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsBlockedException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.request.auth.ChangePasswordRequest;
import com.kamiljach.devjobshub.request.auth.LoginRequest;
import com.kamiljach.devjobshub.request.auth.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(LoginRequest loginRequest) throws UserNotFoundByEmailException;

    public LoginResponse register(RegisterRequest registerRequest) throws AccountAlreadyExistsException;

    public LoginResponse changePasswordByJwt(ChangePasswordRequest changePasswordRequest, String jwt) throws JwtIsBlockedException;
}
