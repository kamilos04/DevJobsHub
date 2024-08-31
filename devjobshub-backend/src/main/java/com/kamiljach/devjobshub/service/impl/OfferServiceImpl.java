package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.service.UtilityClass;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.service.OfferService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;
    private UserRepository userRepository;
    private TechnologyRepository technologyRepository;


    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, TechnologyRepository technologyRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
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


//    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt){
//        Offer newOffer = OfferMapper.INSTANCE.createOfferRequestToOffer(createOfferRequest);
//
//    }

    @Transactional
    public void addCandidate(Offer offer, User user){
        offer.addCandidate(user);
        offerRepository.save(offer);
        userRepository.save(user);
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
