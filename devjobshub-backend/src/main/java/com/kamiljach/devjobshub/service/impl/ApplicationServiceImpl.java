package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.ApplicationNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
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

    public ApplicationDto getApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if(optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}
        return ApplicationDto.mapApplicationToApplicationDto(optionalApplication.get());
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        if(optionalApplication.isEmpty()){throw new ApplicationNotFoundByIdException();}
        Application application = optionalApplication.get();
        removeOfferFromApplication(application);
        removeUserFromApplication(application);
        applicationRepository.delete(application);

    }

    @Transactional
    public void setOfferInApplication(Application application, Offer offer){
        application.setOffer(offer);
        applicationRepository.save(application);
        offerRepository.save(offer);
    }

    @Transactional
    public void setUserInApplication(Application application, User user){
        application.setUser(user);
        applicationRepository.save(application);
        userRepository.save(user);
    }

    @Transactional
    public void removeOfferFromApplication(Application application){
        Offer offer = application.getOffer();
        application.removeOffer();
        offerRepository.save(offer);
        applicationRepository.save(application);
    }

    @Transactional
    public void removeUserFromApplication(Application application){
        User user = application.getUser();
        application.removeUser();
        userRepository.save(user);
        applicationRepository.save(application);
    }
}
