package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.mappers.ApplicationMapper;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    private OfferRepository offerRepository;

    private ApplicationRepository applicationRepository;

    private UserRepository userRepository;
    private UserService userService;

    public ApplicationServiceImpl(OfferRepository offerRepository, ApplicationRepository applicationRepository, UserRepository userRepository, UserService userService) {
        this.offerRepository = offerRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional(rollbackOn = Exception.class)
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException {
        Optional<Offer> optionalOffer = offerRepository.findById(offerId);
        if(optionalOffer.isEmpty()){throw new OfferNotFoundByIdException();}
        Application newApplication = ApplicationMapper.INSTANCE.createApplicationRequestToApplication(createApplicationRequest);
        setUserInApplication(newApplication, userService.findUserByJwt(jwt));
        setOfferInApplication(newApplication, optionalOffer.get());
        applicationRepository.save(newApplication);
        return ApplicationDto.mapApplicationToApplicationDto(newApplication);
    }

    @Transactional
    public void setOfferInApplication(Application application, Offer offer){
        application.setOffer(offer);
        applicationRepository.save(application);
        offerRepository.save(offer);
    }

    public void setUserInApplication(Application application, User user){
        application.setUser(user);
        applicationRepository.save(application);
        userRepository.save(user);
    }
}
