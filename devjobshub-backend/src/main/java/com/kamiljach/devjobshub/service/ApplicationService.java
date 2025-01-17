package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.response.PageResponse;

public interface ApplicationService {
    public ApplicationDto applyForOffer(CreateApplicationRequest createApplicationRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, QuestionOrAnswerIsIncorrectException, OfferExpiredException, FirmAccountCanNotDoThatException, UserAlreadyAppliedForThisOfferException;
    public ApplicationDto getApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException;

    public void deleteApplication(Application application);
    public void deleteApplicationById(Long id, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException;

    public PageResponse<ApplicationDto> getApplicationsFromOffer(Long offerId, Integer numberOfElements, Integer pageNumber, String jwt) throws OfferNotFoundByIdException, NoPermissionException;

    public PageResponse<ApplicationDto> getFavouriteApplicationsFromOffer(Long offerId, Integer numberOfElements, Integer pageNumber, String jwt) throws OfferNotFoundByIdException, NoPermissionException;
}
