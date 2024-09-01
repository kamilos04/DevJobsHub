package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.OfferService;
import com.kamiljach.devjobshub.service.TechnologyService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    private TechnologyRepository technologyRepository;
    private OfferService offerService;

    private OfferRepository offerRepository;


    public TechnologyServiceImpl(TechnologyRepository technologyRepository, OfferService offerService, OfferRepository offerRepository) {
        this.technologyRepository = technologyRepository;
        this.offerService = offerService;
        this.offerRepository = offerRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public TechnologyDto createTechnology(CreateTechnologyRequest technologyRequest, String jwt) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException {
        Optional<Technology> optionalTechnology = technologyRepository.findByName(technologyRequest.getName());
        if(optionalTechnology.isPresent()){throw new TechnologyWithThisNameAlreadyExistsException();}

        Technology newTechnology = new Technology();
        newTechnology.setName(technologyRequest.getName());
        if(technologyRequest.getAssignedAsNiceToHaveIds() != null ){
            ArrayList<Offer> assignedAsRequiredOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsRequiredIds());
            assignedAsRequiredOffersList.forEach(offer -> addAssignedAsRequired(newTechnology ,offer));
        }
        if(technologyRequest.getAssignedAsNiceToHaveIds() != null){
            ArrayList<Offer> assignedAsNiceToHaveOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsNiceToHaveIds());
            assignedAsNiceToHaveOffersList.forEach(offer -> addAssignedAsNiceToHave(newTechnology, offer));
        }

        technologyRepository.save(newTechnology);
        return new TechnologyDto(newTechnology);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException {
        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByIdException();
        }
        Technology technology = optionalTechnology.get();
        technologyRepository.delete(technology);
    }

    @Transactional(rollbackOn = Exception.class)
    public TechnologyDto updateTechnology(CreateTechnologyRequest technologyRequest, Long id, String jwt) throws TechnologyNotFoundByIdException, OfferNotFoundByIdException {
        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByIdException();
        }
        Technology technology = optionalTechnology.get();
        technology.setName(technologyRequest.getName());

        if(technologyRequest.getAssignedAsNiceToHaveIds() != null ){
            ArrayList<Offer> assignedAsRequiredOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsRequiredIds());
            assignedAsRequiredOffersList.forEach(offer -> addAssignedAsRequired(technology, offer));
        }
        if(technologyRequest.getAssignedAsNiceToHaveIds() != null){
            ArrayList<Offer> assignedAsNiceToHaveOffersList = offerService.getListOfOffersFromTheirIds(technologyRequest.getAssignedAsNiceToHaveIds());
            assignedAsNiceToHaveOffersList.forEach(offer -> addAssignedAsNiceToHave(technology, offer));
        }
        technologyRepository.save(technology);
        return new TechnologyDto(technology);

    }

    public TechnologyDto getTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException {
        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if(optionalTechnology.isEmpty()){throw new TechnologyNotFoundByIdException();}
        return new TechnologyDto(optionalTechnology.get());
    }

    @Transactional
    public void addAssignedAsNiceToHave(Technology technology ,Offer offer){
        technology.addToAssignedAsNiceToHave(offer);
        technologyRepository.save(technology);
        offerRepository.save(offer);
    }

    @Transactional
    public void addAssignedAsRequired(Technology technology, Offer offer){
        technology.addToAssignedAsRequired(offer);
        technologyRepository.save(technology);
        offerRepository.save(offer);
    }
}
