package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;

import java.util.ArrayList;

public interface OfferService {

    public OfferDto createOffer(CreateOfferRequest createOfferRequest, String jwt) throws TechnologyNotFoundByIdException;

    public OfferDto updateOffer(CreateOfferRequest createOfferRequest, Long offerId, String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException;

    public OfferDto getOffer(Long offerId, String jwt) throws OfferNotFoundByIdException;

    public ArrayList<OfferDto> searchOffer(SearchOffersRequest searchOffersRequest, String jwt);

    public void deleteOfferById(Long id, String jwt) throws OfferNotFoundByIdException;

    public void likeOffer(Long id, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, OfferIsAlreadyLikedByUserException;

    public void removeLikeOffer(Long id, String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, OfferIsNotLikedByUserException;
}
