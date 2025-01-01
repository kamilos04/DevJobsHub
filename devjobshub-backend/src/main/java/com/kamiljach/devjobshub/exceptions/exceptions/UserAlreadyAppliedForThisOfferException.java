package com.kamiljach.devjobshub.exceptions.exceptions;

public class UserAlreadyAppliedForThisOfferException extends Exception{
    public UserAlreadyAppliedForThisOfferException() {
        super("User already applied for this offer");
    }
}
