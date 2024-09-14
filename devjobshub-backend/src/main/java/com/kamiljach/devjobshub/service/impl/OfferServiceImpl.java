package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.service.TechnologyService;
import com.kamiljach.devjobshub.service.UtilityClass;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.service.OfferService;
import com.kamiljach.devjobshub.service.UtilityService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private TechnologyRepository technologyRepository;

    private UtilityService utilityService;


    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, TechnologyRepository technologyRepository, UtilityService utilityService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.utilityService = utilityService;
    }

    @Transactional(rollbackOn = Exception.class)
    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt) throws TechnologyNotFoundByIdException {
        Offer newOffer = OfferMapper.INSTANCE.createOfferRequestToOffer(createOfferRequest);
        newOffer.setDateTimeOfCreation(LocalDateTime.now());

        if(createOfferRequest.getRequiredTechnologies() != null){
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> addRequiredTechnology(newOffer, element));
        }
        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> addNiceToHaveTechnology(newOffer, element));
        }

        offerRepository.save(newOffer);
        return OfferDto.mapOfferToOfferDto(newOffer);
    }


    @Transactional
    public void addLikedByUser(Offer offer, User user){
        offer.addLikedByUser(user);
        offerRepository.save(offer);
        userRepository.save(user);
    }

    @Transactional
    public void addRequiredTechnology(Offer offer, Technology technology){
        offer.addRequiredTechnology(technology);
        offerRepository.save(offer);
        technologyRepository.save(technology);
    }

    @Transactional
    public void addNiceToHaveTechnology(Offer offer, Technology technology){
        offer.addNiceToHaveTechnology(technology);
        offerRepository.save(offer);
        technologyRepository.save(technology);
    }

}
