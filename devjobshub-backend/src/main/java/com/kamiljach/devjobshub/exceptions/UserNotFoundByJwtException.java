package com.kamiljach.devjobshub.exceptions;

public class UserNotFoundByJwtException extends Exception{
    public UserNotFoundByJwtException() {
        super("User not found, invalid jwt");
    }
}
