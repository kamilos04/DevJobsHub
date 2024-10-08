package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
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
//        Offer newOffer = OfferMapper.INSTANCE.createOfferRequestToOffer(createOfferRequest);
        Offer newOffer = Offer.mapCreateOfferRequestToOffer(createOfferRequest);
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

    @Transactional(rollbackOn = Exception.class)
    public OfferDto updateOffer(CreateOfferRequest createOfferRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        Offer offer = putValuesFromCreateOfferRequestToOffer(createOfferRequest, optionalOffer.get());


        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            for(int i = offer.getNiceToHaveTechnologies().size()-1; i >= 0; i--){
                deleteNiceToHaveTechnologyFromOffer(offer, offer.getNiceToHaveTechnologies().get(i));
            }
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> addNiceToHaveTechnology(offer, element));
        }

        if(createOfferRequest.getRequiredTechnologies() != null){
            for(int i = offer.getRequiredTechnologies().size()-1; i >= 0; i--){
                deleteRequiredTechnologyFromOffer(offer, offer.getRequiredTechnologies().get(i));
            }
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> addRequiredTechnology(offer, element));
        }

        offerRepository.save(offer);

        return OfferDto.mapOfferToOfferDto(offer);

    }

    public OfferDto getOffer(Long offerId, String jwt) throws OfferNotFoundByIdException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        return OfferDto.mapOfferToOfferDto(optionalOffer.get());
    }

    private Offer putValuesFromCreateOfferRequestToOffer(CreateOfferRequest createOfferRequest, Offer offer) {
        offer.setName(createOfferRequest.getName());
        offer.setJobLevel(createOfferRequest.getJobLevel());
        offer.setOperatingMode(createOfferRequest.getOperatingMode());
        offer.setMinSalary(createOfferRequest.getMinSalary());
        offer.setMaxSalary(createOfferRequest.getMaxSalary());
        offer.setIsSalaryMonthly(createOfferRequest.getIsSalaryMonthly());
        offer.setLocalization(createOfferRequest.getLocalization());
        offer.setAboutProject(createOfferRequest.getAboutProject());
        offer.setResponsibilitiesText(createOfferRequest.getResponsibilitiesText());
        offer.setResponsibilities(createOfferRequest.getResponsibilities());
        offer.setRequirements(createOfferRequest.getRequirements());
        offer.setNiceToHave(createOfferRequest.getNiceToHave());
        offer.setWhatWeOffer(createOfferRequest.getWhatWeOffer());
        offer.setQuestions(createOfferRequest.getQuestions());
        offer.setRadioQuestions(createOfferRequest.getRadioQuestions());
        offer.setMultipleChoiceQuestions(createOfferRequest.getMultipleChoiceQuestions());
        if(createOfferRequest.getIsActive()!=null){
            offer.setIsActive(createOfferRequest.getIsActive());
        }
        return offer;
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

    @Transactional
    public void deleteNiceToHaveTechnologyFromOffer(Offer offer, Technology technology){
        technology.deleteOfferAssignedAsNiceToHave(offer);
        technologyRepository.save(technology);
        offerRepository.save(offer);
    }

    @Transactional
    public void deleteRequiredTechnologyFromOffer(Offer offer, Technology technology){
        technology.deleteOfferAssignedAsRequired(offer);
        technologyRepository.save(technology);
        offerRepository.save(offer);
    }
}
