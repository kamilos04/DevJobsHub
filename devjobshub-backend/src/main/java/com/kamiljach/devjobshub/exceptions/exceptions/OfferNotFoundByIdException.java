package com.kamiljach.devjobshub.exceptions.exceptions;

public class OfferNotFoundByIdException extends Exception{
    public OfferNotFoundByIdException() {
        super("Offer not found, invalid ID");
    }
}
