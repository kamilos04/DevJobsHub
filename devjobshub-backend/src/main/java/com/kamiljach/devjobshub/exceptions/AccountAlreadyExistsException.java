package com.kamiljach.devjobshub.exceptions;

public class AccountAlreadyExistsException extends Exception{
    public AccountAlreadyExistsException(){
        super("Account already exists!");
    }
}
