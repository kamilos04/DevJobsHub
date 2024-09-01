package com.kamiljach.devjobshub.mappers;

import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    Offer createOfferRequestToOffer(CreateOfferRequest createOfferRequest);
}
