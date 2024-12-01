package com.kamiljach.devjobshub.exceptions.exceptions;

public class UserIsNotRecruiterException extends Exception{
    public UserIsNotRecruiterException() {
        super("User is not a recruiter");
    }
}
