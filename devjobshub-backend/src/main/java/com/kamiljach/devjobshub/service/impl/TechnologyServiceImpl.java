package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.UtilityClass;
import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.service.OfferService;
import com.kamiljach.devjobshub.service.TechnologyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TechnologyServiceImpl implements TechnologyService {
//    public Technology getTechnologyByName(String name){
//
//    }

    private TechnologyRepository technologyRepository;
    private OfferService offerService;



    public TechnologyServiceImpl(TechnologyRepository technologyRepository, OfferService offerService) {
        this.technologyRepository = technologyRepository;
        this.offerService = offerService;
    }


    public TechnologyDto createTechnology(String name, ArrayList<Long> assignedAsRequiredOffersListId, ArrayList<Long> assignedAsNiceToHaveOffersListId) throws OfferNotFoundByIdException {
        Technology newTechnology = new Technology();
        newTechnology.setName(name);
        ArrayList<Offer> assignedAsRequiredOffersList = offerService.getListOfOffersFromTheirIds(assignedAsRequiredOffersListId);
        ArrayList<Offer> assignedAsNiceToHaveOffersList = offerService.getListOfOffersFromTheirIds(assignedAsNiceToHaveOffersListId);
        newTechnology.setAssignedAsRequired(assignedAsRequiredOffersList);
        newTechnology.setAssignedAsNiceToHave(assignedAsNiceToHaveOffersList);
        technologyRepository.save(newTechnology);
        return new TechnologyDto(newTechnology);
    }



}
