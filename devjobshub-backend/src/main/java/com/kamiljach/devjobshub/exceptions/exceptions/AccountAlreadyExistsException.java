package com.kamiljach.devjobshub.exceptions.exceptions;

public class AccountAlreadyExistsException extends Exception{
    public AccountAlreadyExistsException(){
        super("Account already exists!");
    }
}
