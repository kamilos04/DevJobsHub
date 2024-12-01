package com.kamiljach.devjobshub.exceptions.exceptions;

public class UserNotFoundByIdException extends Exception{
    public UserNotFoundByIdException() {
        super("User not found, invalid ID");
    }
}
