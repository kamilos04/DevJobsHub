package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.model.Technology;

import java.util.ArrayList;

public interface TechnologyService {
//    public Technology getTechnologyByName(String name);
public TechnologyDto createTechnology(String name, ArrayList<Long> assignedAsRequiredOffersListId, ArrayList<Long> assignedAsNiceToHaveOffersListId) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException;
}
