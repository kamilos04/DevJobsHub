package com.kamiljach.devjobshub.exceptions.exceptions;

public class OfferIsAlreadyLikedByUserException extends Exception{
    public OfferIsAlreadyLikedByUserException() {
        super("Offer is already liked by user");
    }
}
