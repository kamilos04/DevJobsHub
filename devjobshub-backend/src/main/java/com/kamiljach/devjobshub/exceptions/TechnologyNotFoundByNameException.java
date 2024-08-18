package com.kamiljach.devjobshub.exceptions;

public class TechnologyNotFoundByNameException extends Exception{
    public TechnologyNotFoundByNameException() {
        super("Technology not found by name, invalid name");
    }
}
