package com.kamiljach.devjobshub.exceptions.exceptions;

public class TechnologyNotFoundByIdException extends Exception{
    public TechnologyNotFoundByIdException() {
        super("Technology not found by ID");
    }
}
