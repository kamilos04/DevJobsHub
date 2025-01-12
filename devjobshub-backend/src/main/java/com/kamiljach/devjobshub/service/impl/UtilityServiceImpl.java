package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.UtilityClass;
import com.kamiljach.devjobshub.service.UtilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UtilityServiceImpl implements UtilityService {
    private OfferRepository offerRepository;
    private TechnologyRepository technologyRepository;

    private UserRepository userRepository;

    private ApplicationRepository applicationRepository;

    public UtilityServiceImpl(OfferRepository offerRepository, TechnologyRepository technologyRepository, UserRepository userRepository, ApplicationRepository applicationRepository) {
        this.offerRepository = offerRepository;
        this.technologyRepository = technologyRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
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

    @Transactional
    public void deleteApplication(Application application){
        removeOfferFromApplication(application);
        removeUserFromApplication(application);
        applicationRepository.delete(application);

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

    public void isFirmFalseOrThrowException(User user) throws FirmAccountCanNotDoThatException {
        if (user.getIsFirm()) throw new FirmAccountCanNotDoThatException();
    }

    public void isFirmOrThrowException(User user) throws NotFirmAccountCanNotDoThatException {
        if (!user.getIsFirm()) throw new NotFirmAccountCanNotDoThatException();
    }

    public void validatePermissionIsAdmin(User user) throws NoPermissionException {
        if(!user.getIsAdmin()){
            throw new NoPermissionException();
        }
    }
}
