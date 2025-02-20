package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
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
    public ResponseEntity<PresignedUrlResponse> getPresignedUrlForCV(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException {
        return new ResponseEntity<PresignedUrlResponse>(s3Service.getPresignedUrlForCV(offerId, jwt), HttpStatus.OK);
    }
}
