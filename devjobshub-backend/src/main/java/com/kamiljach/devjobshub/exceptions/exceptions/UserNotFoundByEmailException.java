package com.kamiljach.devjobshub.exceptions.exceptions;

public class UserNotFoundByEmailException extends Exception{
    public UserNotFoundByEmailException() {
        super("User not found by email");
    }
}
