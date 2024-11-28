package com.kamiljach.devjobshub.exceptions.exceptions;

public class ApplicationIsNotInFavouritesException extends Exception{
    public ApplicationIsNotInFavouritesException() {
        super("Application is not in favourites");
    }
}
