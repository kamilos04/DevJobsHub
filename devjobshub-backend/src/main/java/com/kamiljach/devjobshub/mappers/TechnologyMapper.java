package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.model.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TechnologyMapper {
    TechnologyMapper INSTANCE = Mappers.getMapper(TechnologyMapper.class);

    @Mapping(target = "assignedAsRequired", ignore = true)
    @Mapping(target = "assignedAsNiceToHave", ignore = true)
    TechnologyDto technologyToTechnologyDto(Technology technology);
}
