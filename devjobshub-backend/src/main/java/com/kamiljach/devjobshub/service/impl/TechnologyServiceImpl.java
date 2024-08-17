package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.UtilityClass;
import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TechnologyServiceImpl {
//    public Technology getTechnologyByName(String name){
//
//    }

    private TechnologyRepository technologyRepository;
    private OfferRepository offerRepository;


    public TechnologyServiceImpl(TechnologyRepository technologyRepository, OfferRepository offerRepository) {
        this.technologyRepository = technologyRepository;
        this.offerRepository = offerRepository;
    }


    public TechnologyDto createTechnology(String name, ArrayList<Long> assignedAsRequiredOffersListId, ArrayList<Long> assignedAsNiceToHaveOffersListId) throws OfferNotFoundByIdException {
        Technology newTechnology = new Technology();
        newTechnology.setName(name);
        ArrayList<Offer> assignedAsRequiredOffersList = getListOfOffersFromTheirIds(assignedAsRequiredOffersListId);
        ArrayList<Offer> assignedAsNiceToHaveOffersList = getListOfOffersFromTheirIds(assignedAsNiceToHaveOffersListId);
        newTechnology.setAssignedAsRequired(assignedAsRequiredOffersList);
        newTechnology.setAssignedAsNiceToHave(assignedAsNiceToHaveOffersList);
        technologyRepository.save(newTechnology);
        return new TechnologyDto(newTechnology);
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

}
