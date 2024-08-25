package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;

import java.util.ArrayList;

public interface OfferService {
    public ArrayList<Offer> getListOfOffersFromTheirIds(ArrayList<Long> list) throws OfferNotFoundByIdException ;

//    public void addTechnologyToRequiredTechnologies(Offer offer , Technology technology);
//
//    public void addTechnologyToNiceToHaveTechnologies(Offer offer, Technology technology);
}
