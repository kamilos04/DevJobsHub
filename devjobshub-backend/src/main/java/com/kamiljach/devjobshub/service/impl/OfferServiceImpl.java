package com.kamiljach.devjobshub.service.impl;


import com.kamiljach.devjobshub.service.UtilityClass;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
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

//    public void addTechnologyToRequiredTechnologies(Offer offer ,Technology technology){
//        offer.getRequiredTechnologies().add(technology);
//        offerRepository.save(offer);
//    }
//
//    public void addTechnologyToNiceToHaveTechnologies(Offer offer, Technology technology){
//        offer.getNiceToHaveTechnologies().add(technology);
//        offerRepository.save(offer);
//    }
}
