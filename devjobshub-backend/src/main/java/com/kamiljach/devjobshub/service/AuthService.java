package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.response.login.LoginResponse;

public interface AuthService {
    public LoginResponse login(String emailFromRequest, String passwordFromRequest);
}
