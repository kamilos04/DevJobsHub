package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.response.PageResponse;

import java.util.ArrayList;

public interface TechnologyService {
    public TechnologyDto createTechnology(CreateTechnologyRequest technologyRequest, String jwt) throws  TechnologyWithThisNameAlreadyExistsException;
    public void deleteTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException, NoPermissionException;

    public TechnologyDto getTechnologyById(Long id, String jwt) throws TechnologyNotFoundByIdException;

    public TechnologyDto updateTechnology(CreateTechnologyRequest technologyRequest, Long id, String jwt) throws TechnologyNotFoundByIdException, NoPermissionException;

    public PageResponse<TechnologyDto> searchTechnologies(String text);

    public ArrayList<TechnologyDto> getTechnologiesByIds(ArrayList<Long> ids) throws TechnologyNotFoundByIdException;
}
