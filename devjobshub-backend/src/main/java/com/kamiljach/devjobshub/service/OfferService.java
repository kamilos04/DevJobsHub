package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferIsAlreadyLikedByUserException;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
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

    }
