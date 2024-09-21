package com.kamiljach.devjobshub.exceptions;

public class ApplicationNotFoundByIdException extends Exception{
    public ApplicationNotFoundByIdException() {
        super("Application not found, invalid ID");
    }
}
