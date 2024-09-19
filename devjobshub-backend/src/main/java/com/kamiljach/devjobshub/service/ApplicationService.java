package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;

public interface ApplicationService {
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException;
}
