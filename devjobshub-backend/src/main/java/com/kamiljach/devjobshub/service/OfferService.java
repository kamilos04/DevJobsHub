package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;

import java.util.ArrayList;

public interface OfferService {
    public ArrayList<Offer> getListOfOffersFromTheirIds(ArrayList<Long> list) throws OfferNotFoundByIdException ;

//    public void addTechnologyToRequiredTechnologies(Offer offer , Technology technology);
//
//    public void addTechnologyToNiceToHaveTechnologies(Offer offer, Technology technology);

    public void addCandidate(Offer offer, User user);
    public void addLikedByUser(Offer offer, User user);
    public void addRequiredTechnology(Offer offer, Technology technology);
    public void addNiceToHaveTechnology(Offer offer, Technology technology);
}
