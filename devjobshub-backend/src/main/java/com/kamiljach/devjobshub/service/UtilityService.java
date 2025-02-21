package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;

import java.util.ArrayList;

public interface UtilityService {
    public ArrayList<Technology> getListOfTechnologiesFromTheirIds(ArrayList<Long> list) throws TechnologyNotFoundByIdException;
    public ArrayList<Offer> getListOfOffersFromTheirIds(ArrayList<Long> list) throws OfferNotFoundByIdException;


    public void isFirmFalseOrThrowException(User user) throws FirmAccountCanNotDoThatException;

    public void isFirmOrThrowException(User user) throws NoFirmAccountCanNotDoThatException;

    public void validatePermissionIsAdmin(User user) throws NoPermissionException;
}
