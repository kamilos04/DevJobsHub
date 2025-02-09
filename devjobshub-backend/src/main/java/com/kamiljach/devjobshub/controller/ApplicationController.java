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
    public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable("applicationId") Long applicationId, @RequestHeader("Authorization")String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        return new ResponseEntity<ApplicationDto>(applicationService.getApplicationById(applicationId, jwt), HttpStatus.OK);
    }

    @DeleteMapping("/application/{applicationId}")
    public ResponseEntity<MessageResponse> deleteApplicationById(@PathVariable("applicationId") Long applicationId, @RequestHeader("Authorization")String jwt) throws ApplicationNotFoundByIdException, NoPermissionException {
        applicationService.deleteApplicationById(applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Deleted application");
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/application/from-offer/{offerId}")
    public ResponseEntity<PageResponse<ApplicationDto>> getApplicationsFromOffer(@PathVariable("offerId") Long offerId,
                                                                                 @RequestParam("numberOfElements") Integer numberOfElements,
                                                                                 @RequestParam("pageNumber") Integer pageNumber,
                                                                                 @RequestParam("isFavourite") Boolean isFavourite,
                                                                                 @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException, NoPermissionException {
        PageResponse<ApplicationDto> pageResponse = applicationService.getApplicationsFromOffer(offerId, numberOfElements, pageNumber, isFavourite, jwt);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }


    @PostMapping("/application/add-to-favourites/{applicationId}")
    public ResponseEntity<MessageResponse> addApplicationToFavourites(@PathVariable("applicationId") Long applicationId,
                                                                      @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException, NoPermissionException {
        applicationService.addApplicationToFavourites(applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been added to favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    @PostMapping("/application/remove-from-favourites/{applicationId}")
    public ResponseEntity<MessageResponse> removeApplicationFromFavourites(@PathVariable("applicationId") Long applicationId,
                                                                           @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, ApplicationIsNotInFavouritesException, NoPermissionException {
        applicationService.removeApplicationFromFavourites(applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been removed from favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
