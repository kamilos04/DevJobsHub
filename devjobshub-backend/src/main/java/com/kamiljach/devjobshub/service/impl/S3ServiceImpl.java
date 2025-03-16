package com.kamiljach.devjobshub.service.impl;

import com.kamiljach.devjobshub.config.AwsConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.response.PresignedUrlResponse;
import com.kamiljach.devjobshub.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class S3ServiceImpl implements S3Service {
    private AwsConfig awsConfig;
    private OfferRepository offerRepository;
    private UserService userService;

    private ApplicationService applicationService;

    private ApplicationRepository applicationRepository;

    private OfferService offerService;

    private UtilityService utilityService;


    public S3ServiceImpl(AwsConfig awsConfig, OfferRepository offerRepository, UserService userService, ApplicationService applicationService, ApplicationRepository applicationRepository, OfferService offerService, UtilityService utilityService) {
        this.awsConfig = awsConfig;
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.offerService = offerService;
        this.utilityService = utilityService;
    }

    public String createPresignedUrlPut(String keyName, Map<String, String> metadata) {


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

            return presignedRequest.url().toExternalForm();
    }


    public String createPresignedUrlPut_PublicFile(String keyName, Map<String, String> metadata) {


        AwsRequestOverrideConfiguration override = AwsRequestOverrideConfiguration.builder()
                .putRawQueryParameter("x-amz-acl", "public-read")
                .build();


        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(awsConfig.bucketName)
                .key(keyName)
                .metadata(metadata)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .overrideConfiguration(override)
                .build();



        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = awsConfig.s3Presigner().presignPutObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }


    public String createPresignedUrlGet(String keyName) {


        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(awsConfig.bucketName)
                .key(keyName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = awsConfig.s3Presigner().presignGetObject(presignRequest);

        return presignedRequest.url().toExternalForm();
    }


    @Transactional(rollbackFor = Exception.class)
    public PresignedUrlResponse getPresignedUrlForCV(Long offerId, String fileExtension, String jwt) throws OfferNotFoundByIdException, UserAlreadyAppliedForThisOfferException, FirmAccountCanNotDoThatException {
        Offer offer = offerRepository.findById(offerId).orElseThrow(OfferNotFoundByIdException::new);

        User user = userService.findUserByJwt(jwt);
        utilityService.isFirmFalseOrThrowException(user);
        applicationService.ifUserAlreadyAppliedForOfferThrowException(user, offer);

        String key = String.format("cv/%d-%d.%s", offer.getId(), user.getId(), fileExtension);

        PresignedUrlResponse response = new PresignedUrlResponse();
        response.setUrl(createPresignedUrlPut(key, new HashMap<String, String>()));
        response.setKey(key);
        return response;
    }


    @Transactional(rollbackFor = Exception.class)
    public PresignedUrlResponse getPresignedUrlForFirmImage(String fileExtension, String jwt) {

        User user = userService.findUserByJwt(jwt);

        String key = String.format("firm-images/%d.%s", System.currentTimeMillis(), fileExtension);

        PresignedUrlResponse response = new PresignedUrlResponse();
        response.setUrl(createPresignedUrlPut_PublicFile(key, new HashMap<String, String>()));
        response.setKey(key);
        return response;
    }


    public PresignedUrlResponse getPresignedUrlToDownloadCV(Long applicationId, String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        Application application = applicationRepository.findById(applicationId).orElseThrow(ApplicationNotFoundByIdException::new);
        User user = userService.findUserByJwt(jwt);
        applicationService.validatePermissionGetApplicationById(user, application);

        String key = application.getCvUrl();

        PresignedUrlResponse response = new PresignedUrlResponse();
        response.setUrl(createPresignedUrlGet(key));
        response.setKey(key);
        return response;
    }
}
