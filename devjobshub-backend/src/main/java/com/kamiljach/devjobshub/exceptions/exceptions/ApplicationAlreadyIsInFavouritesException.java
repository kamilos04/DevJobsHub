package com.kamiljach.devjobshub.exceptions.exceptions;

public class ApplicationAlreadyIsInFavouritesException extends Exception{
    public ApplicationAlreadyIsInFavouritesException() {
        super("Application already is in favourites");
    }
}
