package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.model.Technology;

public interface TechnologyService {
//    public Technology getTechnologyByName(String name);
    public TechnologyDto createTechnology(String name);
}
