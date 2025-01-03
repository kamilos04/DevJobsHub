package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.response.MessageResponse;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.ApplicationService;
import com.kamiljach.devjobshub.service.UtilityService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApplicationDto> applyForOffer(@PathVariable("offerId") Long offerId, @Valid @RequestBody CreateApplicationRequest createApplicationRequest, @RequestHeader("Authorization")String jwt) throws UserNotFoundByJwtException, OfferNotFoundByIdException, QuestionOrAnswerIsIncorrectException, OfferExpiredException, FirmAccountCanNotDoThatException, UserAlreadyAppliedForThisOfferException {
        return new ResponseEntity<>(applicationService.applyForOffer(createApplicationRequest, offerId, jwt), HttpStatus.OK);
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable("applicationId") Long applicationId, @RequestHeader("Authorization")String jwt) throws ApplicationNotFoundByIdException {
        return new ResponseEntity<ApplicationDto>(applicationService.getApplicationById(applicationId, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/application/{applicationId}")
    public ResponseEntity<MessageResponse> deleteApplicationById(@PathVariable("applicationId") Long applicationId, @RequestHeader("Authorization")String jwt) throws ApplicationNotFoundByIdException {
        applicationService.deleteApplicationById(applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Deleted application");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/application/fromOffer/{offerId}")
    public ResponseEntity<PageResponse<ApplicationDto>> getApplicationsFromOffer(@PathVariable("offerId") Long offerId, @RequestParam("numberOfElements") Integer numberOfElements, @RequestParam("pageNumber") Integer pageNumber, @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException {
        PageResponse<ApplicationDto> pageResponse = applicationService.getApplicationsFromOffer(offerId, numberOfElements, pageNumber, jwt);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }
}
