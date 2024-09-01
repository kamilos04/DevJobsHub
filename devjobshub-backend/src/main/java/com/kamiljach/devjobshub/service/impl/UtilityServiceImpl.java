package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.TechnologyService;
import com.kamiljach.devjobshub.service.UtilityClass;
import com.kamiljach.devjobshub.service.UtilityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UtilityServiceImpl implements UtilityService {
    private OfferRepository offerRepository;
    private TechnologyRepository technologyRepository;

    private UserRepository userRepository;

    public UtilityServiceImpl(OfferRepository offerRepository, TechnologyRepository technologyRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.technologyRepository = technologyRepository;
        this.userRepository = userRepository;
    }

    public ArrayList<Technology> getListOfTechnologiesFromTheirIds(ArrayList<Long> list) throws TechnologyNotFoundByIdException {
        ArrayList<Long> listWithIds = UtilityClass.removeRepetitionLong(list);
        ArrayList<Technology> technologyList = new ArrayList<>();
        for(int i = 0; i < listWithIds.size(); i++){
            Optional<Technology> optionalTechnology = technologyRepository.findById(listWithIds.get(i));
            if(optionalTechnology.isPresent()){
                technologyList.add(optionalTechnology.get());
            }
            else{
                throw new TechnologyNotFoundByIdException();
            }
        }
        return technologyList;
    }

    public ArrayList<Offer> getListOfOffersFromTheirIds(ArrayList<Long> list) throws OfferNotFoundByIdException {
        ArrayList<Long> listWithIds = UtilityClass.removeRepetitionLong(list);
        ArrayList<Offer> offerList = new ArrayList<>();
        for(int i = 0; i < listWithIds.size(); i++) {
            Optional<Offer> optionalOffer = offerRepository.findById(listWithIds.get(i));
            if(optionalOffer.isPresent()){
                offerList.add(optionalOffer.get());
            }
            else{
                throw new OfferNotFoundByIdException();

            }
        }
        return offerList;
    }
}
