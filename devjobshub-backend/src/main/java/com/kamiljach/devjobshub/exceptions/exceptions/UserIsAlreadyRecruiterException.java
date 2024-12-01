package com.kamiljach.devjobshub.exceptions.exceptions;

public class UserIsAlreadyRecruiterException extends Exception{
    public UserIsAlreadyRecruiterException() {
        super("User is already a recruiter");
    }
}
