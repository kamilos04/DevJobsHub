package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.model.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TechnologyMapper {
    TechnologyMapper INSTANCE = Mappers.getMapper(TechnologyMapper.class);

    TechnologyDto technologyToTechnologyDto(Technology technology);
}
