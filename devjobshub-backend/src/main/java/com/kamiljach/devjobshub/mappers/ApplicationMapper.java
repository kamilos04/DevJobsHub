package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationMapper {
    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);

    @Mapping(target = "dateTimeOfCreation", ignore = true)
    ApplicationDto applicationToApplicationDto(Application application);

    Application createApplicationRequestToApplication(CreateApplicationRequest createApplicationRequest);
}
