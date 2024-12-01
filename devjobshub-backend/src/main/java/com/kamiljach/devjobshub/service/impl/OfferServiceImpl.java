package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.service.OfferService;
import com.kamiljach.devjobshub.service.UserService;
import com.kamiljach.devjobshub.service.UtilityService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private TechnologyRepository technologyRepository;

    private UtilityService utilityService;

    private UserService userService;

    private ApplicationRepository applicationRepository;

    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, TechnologyRepository technologyRepository, UtilityService utilityService, UserService userService, ApplicationRepository applicationRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
        this.utilityService = utilityService;
        this.userService = userService;
        this.applicationRepository = applicationRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException {
        User user = userService.findUserByJwt(jwt);

        Offer newOffer = Offer.mapCreateOfferRequestToOffer(createOfferRequest);
        newOffer.setDateTimeOfCreation(LocalDateTime.now());

        newOffer.addRecruiter(user);
        userRepository.save(user);

        if(createOfferRequest.getRequiredTechnologies() != null){
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> {
                newOffer.addRequiredTechnology(element);
                technologyRepository.save(element);
            });

        }
        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> {
                newOffer.addNiceToHaveTechnology(element);
                technologyRepository.save(element);
            });
        }

        offerRepository.save(newOffer);
        return Offer.mapOfferToOfferDto(newOffer);
    }

    @Transactional(rollbackFor = Exception.class)
    public OfferDto updateOffer(CreateOfferRequest createOfferRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        Offer offer = Offer.mapCreateOfferRequestToExistingOffer(createOfferRequest, optionalOffer.get());


        if(createOfferRequest.getNiceToHaveTechnologies() != null){
            for(int i = offer.getNiceToHaveTechnologies().size()-1; i >= 0; i--){
                Technology technology = offer.getNiceToHaveTechnologies().get(i);
                offer.removeNiceToHaveTechnology(technology);
                technologyRepository.save(technology);
            }
            ArrayList<Technology> niceToHaveTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getNiceToHaveTechnologies());
            niceToHaveTechnologies.forEach(element -> {
                offer.addNiceToHaveTechnology(element);
                technologyRepository.save(element);
            });
        }

        if(createOfferRequest.getRequiredTechnologies() != null){
            for(int i = offer.getRequiredTechnologies().size()-1; i >= 0; i--){
                Technology technology = offer.getRequiredTechnologies().get(i);
                offer.removeRequiredTechnology(technology);
                technologyRepository.save(technology);
            }
            ArrayList<Technology> requiredTechnologies = utilityService.getListOfTechnologiesFromTheirIds(createOfferRequest.getRequiredTechnologies());
            requiredTechnologies.forEach(element -> {
                offer.addRequiredTechnology(element);
                technologyRepository.save(element);
            });
        }

        offerRepository.save(offer);
        return Offer.mapOfferToOfferDto(offer);

    }

    @Transactional
    public ArrayList<OfferDto> searchOffer(SearchOffersRequest searchOffersRequest, String jwt){
        Pageable pageable;
        if(searchOffersRequest.getSortingDirection().equals("dsc")){
            pageable = PageRequest.of(searchOffersRequest.getPageNumber(), searchOffersRequest.getNumberOfElements(), Sort.by(searchOffersRequest.getSortBy()).descending());
        }
        else{
            pageable = PageRequest.of(searchOffersRequest.getPageNumber(), searchOffersRequest.getNumberOfElements(), Sort.by(searchOffersRequest.getSortBy()).ascending());
        }

        ArrayList<Offer> offers = new ArrayList<>(offerRepository.searchOffers(searchOffersRequest.getText(), searchOffersRequest.getJobLevels(), searchOffersRequest.getOperatingModes(), searchOffersRequest.getLocalizations(), searchOffersRequest.getTechnologies(), pageable).getContent());

        ArrayList<OfferDto> offersDto = new ArrayList<>();
        offers.forEach(element -> {offersDto.add(Offer.mapOfferToOfferDtoShallow(element));});
        return offersDto;
    }

    public OfferDto getOffer(Long offerId, String jwt) throws OfferNotFoundByIdException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        return Offer.mapOfferToOfferDto(optionalOffer.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOfferById(Long id, String jwt) throws OfferNotFoundByIdException{
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        Offer offer = optionalOffer.get();

        for(int i = offer.getLikedByUsers().size()-1; i>=0; i--){
            User user = offer.getLikedByUsers().get(i);
            user.removeLikedOffer(offer);
            userRepository.save(user);
        }

        for(int i = offer.getRequiredTechnologies().size()-1; i>=0; i--){
            Technology technology = offer.getRequiredTechnologies().get(i);
            offer.removeRequiredTechnology(technology);
            technologyRepository.save(technology);
        }

        for(int i = offer.getNiceToHaveTechnologies().size()-1; i>=0; i--){
            Technology technology = offer.getNiceToHaveTechnologies().get(i);
            offer.removeNiceToHaveTechnology(technology);
            technologyRepository.save(technology);
        }

        for(int i = offer.getApplications().size()-1; i>=0; i--){
            Application application = offer.getApplications().get(i);
            utilityService.deleteApplication(application);
        }

        offerRepository.delete(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void likeOffer(Long id, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, OfferIsAlreadyLikedByUserException {
        User user = userService.findUserByJwt(jwt);
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}

        Offer offer = optionalOffer.get();
        if (user.getLikedOffers().contains(offer)){
            throw new OfferIsAlreadyLikedByUserException();
        }
        user.addLikedOffer(offer);
        userRepository.save(user);
        offerRepository.save(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeLikeOffer(Long id, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, OfferIsNotLikedByUserException {
        User user = userService.findUserByJwt(jwt);
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}

        Offer offer = optionalOffer.get();
        if (!user.getLikedOffers().contains(offer)){
            throw new OfferIsNotLikedByUserException();
        }
        user.removeLikedOffer(offer);
        userRepository.save(user);
        offerRepository.save(offer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addApplicationToFavourites(Long offerId, Long applicationId, String jwt) throws OfferNotFoundByIdException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);

        if (optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        if (optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}

        Offer offer = optionalOffer.get();
        Application application = optionalApplication.get();

        if(offer.getFavouriteApplications().contains(application)){throw new ApplicationAlreadyIsInFavouritesException();}

        offer.addFavouriteApplication(application);
        offerRepository.save(offer);
        applicationRepository.save(application);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeApplicationFromFavourites(Long offerId, Long applicationId, String jwt) throws OfferNotFoundByIdException, ApplicationNotFoundByIdException, ApplicationIsNotInFavouritesException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);

        if (optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        if (optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}

        Offer offer = optionalOffer.get();
        Application application = optionalApplication.get();

        if(!offer.getFavouriteApplications().contains(application)){throw new ApplicationIsNotInFavouritesException();}

        offer.removeFavouriteApplication(application);
        offerRepository.save(offer);
        applicationRepository.save(application);

    }


}
