package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.response.PresignedUrlResponse;
import com.kamiljach.devjobshub.service.S3Service;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class S3Controller {

    private S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/presigned-cv/{offerId}")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlForCV(@PathVariable("offerId") Long offerId, @RequestParam("fileExtension") String fileExtension, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, UserAlreadyAppliedForThisOfferException, FirmAccountCanNotDoThatException {
        return new ResponseEntity<PresignedUrlResponse>(s3Service.getPresignedUrlForCV(offerId, fileExtension, jwt), HttpStatus.OK);
    }

    @GetMapping("/presigned-firm-image")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlForFirmImage(@RequestParam("fileExtension") String fileExtension, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, UserAlreadyAppliedForThisOfferException, NoFirmAccountCanNotDoThatException {
        return new ResponseEntity<>(s3Service.getPresignedUrlForFirmImage(fileExtension, jwt), HttpStatus.OK);
    }


    @GetMapping("/presigned-cv-download/{applicationId}")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlToDownloadCV(@PathVariable("applicationId") Long applicationId, @RequestHeader("Authorization")String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        return new ResponseEntity<PresignedUrlResponse>(s3Service.getPresignedUrlToDownloadCV(applicationId, jwt), HttpStatus.OK);
    }

}
