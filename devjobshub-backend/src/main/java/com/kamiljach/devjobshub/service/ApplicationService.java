package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.ApplicationNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.QuestionOrAnswerIsIncorrectException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;

public interface ApplicationService {
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, QuestionOrAnswerIsIncorrectException;
    public ApplicationDto getApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException;


    public void deleteApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException;
}
