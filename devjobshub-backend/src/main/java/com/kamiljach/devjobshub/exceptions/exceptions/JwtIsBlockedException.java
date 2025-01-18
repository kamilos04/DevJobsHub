package com.kamiljach.devjobshub.exceptions.exceptions;

public class JwtIsBlockedException extends Exception{
    public JwtIsBlockedException() {
        super("JWT token is blocked");
    }
}
