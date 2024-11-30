package com.kamiljach.devjobshub.exceptions.exceptions;

public class OfferExpiredException extends Exception{
    public OfferExpiredException() {
        super("Offer has already expired");
    }
}
