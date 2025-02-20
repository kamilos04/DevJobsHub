package com.kamiljach.devjobshub.service;


import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserAlreadyAppliedForThisOfferException;
import com.kamiljach.devjobshub.response.PresignedUrlResponse;

import java.util.Map;

public interface S3Service {
    public String createPresignedUrl(String keyName, Map<String, String> metadata);

    public PresignedUrlResponse getPresignedUrlForCV(Long offerId, String fileExtension, String jwt) throws OfferNotFoundByIdException, UserAlreadyAppliedForThisOfferException;
}
