package com.kamiljach.devjobshub.exceptions.exceptions;

public class JwtIsOnBlackListException extends Exception{
    public JwtIsOnBlackListException() {
        super("Jwt is on black list");
    }
}
