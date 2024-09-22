package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);


    @Mapping(target = "requiredTechnologies", ignore = true)
    @Mapping(target = "niceToHaveTechnologies", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTimeOfCreation", ignore = true)
    Offer createOfferRequestToOffer(CreateOfferRequest createOfferRequest);


    @Mapping(target = "requiredTechnologies", ignore = true)
    @Mapping(target = "niceToHaveTechnologies", ignore = true)
    @Mapping(target = "likedByUsers", ignore = true)
    @Mapping(target = "dateTimeOfCreation", ignore = true)
    OfferDto offerToOfferDto(Offer offer);
}
