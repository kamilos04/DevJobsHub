package com.kamiljach.devjobshub.exceptions;

public class TechnologyWithThisNameAlreadyExistsException extends Exception{
    public TechnologyWithThisNameAlreadyExistsException() {
        super("Technology with this name already exists");
    }
}
