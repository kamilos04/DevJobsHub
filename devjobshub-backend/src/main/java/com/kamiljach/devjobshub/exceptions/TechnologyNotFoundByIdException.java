package com.kamiljach.devjobshub.exceptions;

public class TechnologyNotFoundByIdException extends Exception{
    public TechnologyNotFoundByIdException() {
        super("Technology not found by ID");
    }
}
