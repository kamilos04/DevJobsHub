package com.kamiljach.devjobshub.exceptions.exceptions;

public class ApplicationNotFoundByIdException extends Exception{
    public ApplicationNotFoundByIdException() {
        super("Application not found, invalid ID");
    }
}
