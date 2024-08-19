package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.UtilityClass;
import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByNameException;
import com.kamiljach.devjobshub.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.OfferService;
import com.kamiljach.devjobshub.service.TechnologyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    private TechnologyRepository technologyRepository;
    private OfferService offerService;



    public TechnologyServiceImpl(TechnologyRepository technologyRepository, OfferService offerService) {
        this.technologyRepository = technologyRepository;
        this.offerService = offerService;
    }


    public TechnologyDto createTechnology(String name, ArrayList<Long> assignedAsRequiredOffersListId, ArrayList<Long> assignedAsNiceToHaveOffersListId, String jwt) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException {
        Optional<Technology> optionalTechnology = technologyRepository.findByName(name);
        if(optionalTechnology.isPresent()){throw new TechnologyWithThisNameAlreadyExistsException();}

        Technology newTechnology = new Technology();
        newTechnology.setName(name);
        ArrayList<Offer> assignedAsRequiredOffersList = offerService.getListOfOffersFromTheirIds(assignedAsRequiredOffersListId);
        ArrayList<Offer> assignedAsNiceToHaveOffersList = offerService.getListOfOffersFromTheirIds(assignedAsNiceToHaveOffersListId);
        newTechnology.setAssignedAsRequired(assignedAsRequiredOffersList);
        newTechnology.setAssignedAsNiceToHave(assignedAsNiceToHaveOffersList);
        for(int i = 0; i < assignedAsRequiredOffersList.size(); i++){
            offerService.addTechnologyToRequiredTechnologies(assignedAsRequiredOffersList.get(i), newTechnology);
        }
        for(int i = 0; i < assignedAsNiceToHaveOffersList.size(); i++){
            offerService.addTechnologyToNiceToHaveTechnologies(assignedAsNiceToHaveOffersList.get(i), newTechnology);
        }

        technologyRepository.save(newTechnology);
        return new TechnologyDto(newTechnology);
    }

    public void deleteTechnologyByName(String name, String jwt) throws TechnologyNotFoundByNameException {
        Optional<Technology> optionalTechnology = technologyRepository.findByName(name);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByNameException();
        }
        Technology technology = optionalTechnology.get();
        technologyRepository.delete(technology);
    }

    public TechnologyDto updateTechnology(CreateTechnologyRequest technologyRequest, Long id, String jwt) throws TechnologyNotFoundByIdException, OfferNotFoundByIdException {
        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByIdException();
        }
        Technology technology = optionalTechnology.get();
        technology.setName(technologyRequest.getName());

        if(technologyRequest.getAssignedAsNiceToHaveIds() != null ){
            ArrayList<Offer> assignedAsRequiredOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsRequiredIds());
            assignedAsRequiredOffersList.forEach(offer -> technology.addToAssignedAsRequired(offer));
        }
        if(technologyRequest.getAssignedAsNiceToHaveIds() != null){
            ArrayList<Offer> assignedAsNiceToHaveOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsNiceToHaveIds());
            assignedAsNiceToHaveOffersList.forEach(offer -> technology.addToAssignedAsNiceToHave(offer));
        }
        technologyRepository.save(technology);
        return new TechnologyDto(technology);

    }
}
