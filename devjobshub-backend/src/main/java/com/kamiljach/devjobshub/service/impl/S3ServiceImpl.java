package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.AwsConfig;
import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.APPLICATION_STATUS;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.response.PresignedUrlResponse;
import com.kamiljach.devjobshub.service.S3Service;
import com.kamiljach.devjobshub.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.s3.S3Client;


import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3ServiceImpl implements S3Service {
    private AwsConfig awsConfig;
    private OfferRepository offerRepository;
    private UserService userService;


    public S3ServiceImpl(AwsConfig awsConfig, OfferRepository offerRepository, UserService userService) {
        this.awsConfig = awsConfig;
        this.offerRepository = offerRepository;
        this.userService = userService;
    }

    public String createPresignedUrl(String keyName, Map<String, String> metadata) {


            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(awsConfig.bucketName)
                    .key(keyName)
                    .metadata(metadata)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(60))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = awsConfig.s3Presigner().presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();

            return presignedRequest.url().toExternalForm();
    }


    @Transactional(rollbackFor = Exception.class)
    public PresignedUrlResponse getPresignedUrlForCV(Long offerId, String jwt) throws OfferNotFoundByIdException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);

        User user = userService.findUserByJwt(jwt);

        String key = String.format("cv/%d-%d", offer.getId(), user.getId());

        PresignedUrlResponse response = new PresignedUrlResponse();
        response.setUrl(createPresignedUrl(key, new HashMap<String, String>()));
        response.setKey(key);
        return response;
    }
}
