package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.UserService;
import com.kamiljach.devjobshub.service.UtilityService;
import com.kamiljach.devjobshub.service.TechnologyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    private TechnologyRepository technologyRepository;

    private OfferRepository offerRepository;

    private UserService userService;
    
    private UtilityService utilityService;


    public TechnologyServiceImpl(TechnologyRepository technologyRepository, OfferRepository offerRepository, UserService userService, UtilityService utilityService) {
        this.technologyRepository = technologyRepository;
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.utilityService = utilityService;
    }

    @Transactional(rollbackFor = Exception.class)
    public TechnologyDto createTechnology(@Valid CreateTechnologyRequest technologyRequest, String jwt) throws TechnologyWithThisNameAlreadyExistsException{
        Optional<Technology> optionalTechnology = technologyRepository.findByName(technologyRequest.getName());
        if(optionalTechnology.isPresent()){throw new TechnologyWithThisNameAlreadyExistsException();}

        Technology newTechnology = new Technology();
        newTechnology.setName(technologyRequest.getName());

        technologyRepository.save(newTechnology);
        return Technology.mapTechnologyToTechnologyDtoShallow(newTechnology);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);
        utilityService.validatePermissionIsAdmin(user);

        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByIdException();
        }
        Technology technology = optionalTechnology.get();

        for(int i = technology.getAssignedAsRequired().size()-1; i >= 0; i--){
            Offer offer = technology.getAssignedAsRequired().get(i);
            technology.removeOfferAssignedAsRequired(offer);
            offerRepository.save(offer);
        }
        for(int i = technology.getAssignedAsNiceToHave().size()-1; i >= 0; i--){
            Offer offer = technology.getAssignedAsNiceToHave().get(i);
            technology.removeOfferAssignedAsNiceToHave(offer);
            offerRepository.save(offer);
        }
        technologyRepository.delete(technology);
    }

    @Transactional(rollbackFor = Exception.class)
    public TechnologyDto updateTechnology(@Valid CreateTechnologyRequest technologyRequest, Long id, String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        User user = userService.findUserByJwt(jwt);
        utilityService.validatePermissionIsAdmin(user);

        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if (optionalTechnology.isEmpty()) {
            throw new TechnologyNotFoundByIdException();
        }
        Technology technology = optionalTechnology.get();
        technology.setName(technologyRequest.getName());
        technologyRepository.save(technology);
        return Technology.mapTechnologyToTechnologyDtoShallow(technology);

    }

    public TechnologyDto getTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException {
        Optional<Technology> optionalTechnology = technologyRepository.findById(id);
        if(optionalTechnology.isEmpty()){throw new TechnologyNotFoundByIdException();}
        return Technology.mapTechnologyToTechnologyDtoShallow(optionalTechnology.get());
    }




}
