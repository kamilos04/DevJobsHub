package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.exceptions.ApplicationNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;

import java.util.ArrayList;

public interface UtilityService {
    public ArrayList<Technology> getListOfTechnologiesFromTheirIds(ArrayList<Long> list) throws TechnologyNotFoundByIdException;
    public ArrayList<Offer> getListOfOffersFromTheirIds(ArrayList<Long> list) throws OfferNotFoundByIdException;

    public void removeOfferFromApplication(Application application);

    public void removeUserFromApplication(Application application);

    public void deleteApplication(Application application);
}
