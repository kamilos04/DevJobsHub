package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.impl.ApplicationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ApplicationController {

    private ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{offerId}")
    public ResponseEntity<ApplicationDto> applyForOffer(@PathVariable("offerId") Long offerId, @RequestBody CreateApplicationRequest createApplicationRequest, @RequestHeader("Authorization")String jwt) throws UserNotFoundByJwtException, OfferNotFoundByIdException {
        return new ResponseEntity<>(applicationService.applyForOffer(createApplicationRequest, offerId, jwt), HttpStatus.OK);
    }
}
