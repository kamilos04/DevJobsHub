package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByNameException;
import com.kamiljach.devjobshub.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;

public interface TechnologyService {
//    public Technology getTechnologyByName(String name);
    public TechnologyDto createTechnology(CreateTechnologyRequest technologyRequest, String jwt) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException;
    public void deleteTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException;
}
