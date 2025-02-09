package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.response.PageResponse;

public interface OfferService {

    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt) throws TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException;

    public OfferDto updateOffer(CreateOfferRequest createOfferRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException, NoPermissionException;

    public OfferDto getOffer(Long offerId, String jwt) throws OfferNotFoundByIdException;

    public PageResponse<OfferDto> searchOffer(SearchOffersRequest searchOffersRequest, String jwt);

    public void deleteOfferById(Long id, String jwt) throws OfferNotFoundByIdException, NoPermissionException;

    public void likeOffer(Long id, String jwt) throws OfferNotFoundByIdException, OfferIsAlreadyLikedByUserException, NoPermissionException;

    public void removeLikeOffer(Long id, String jwt) throws OfferNotFoundByIdException, OfferIsNotLikedByUserException, NoPermissionException;

    public void removeRecruiterFromOffer(Long offerId, Long recruiterId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsNotRecruiterException, NoPermissionException;

    public void addRecruiterToOffer(Long offerId, Long recruiterId, String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsAlreadyRecruiterException, NoPermissionException;

    public PageResponse<OfferDto> getLikedOffersFromJwt(Integer numberOfElements, Integer pageNumber, String jwt) throws UserNotFoundByJwtException;

    public PageResponse<OfferDto> getOffersFromRecruiter(Long recruiterId, Boolean isActive, Integer numberOfElements, Integer pageNumber, String sortBy, String sortDirection, String jwt) throws UserNotFoundByIdException, NoPermissionException;
}
