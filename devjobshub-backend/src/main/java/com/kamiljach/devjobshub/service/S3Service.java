package com.kamiljach.devjobshub.service;


import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.response.PresignedUrlResponse;

import java.util.Map;

public interface S3Service {
    public String createPresignedUrlPut(String keyName, Map<String, String> metadata);

    public PresignedUrlResponse getPresignedUrlForCV(Long offerId, String fileExtension, String jwt) throws OfferNotFoundByIdException, UserAlreadyAppliedForThisOfferException, FirmAccountCanNotDoThatException;
    public PresignedUrlResponse getPresignedUrlToDownloadCV(Long applicationId, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException;

    public PresignedUrlResponse getPresignedUrlForFirmImage(String fileExtension, String jwt) throws NoFirmAccountCanNotDoThatException;
}
