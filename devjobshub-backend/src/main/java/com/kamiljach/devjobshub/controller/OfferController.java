package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.response.MessageResponse;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class OfferController {

    private OfferService offerService;


    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/offer")
    public ResponseEntity<OfferDto> createOffer(@Valid @RequestBody CreateOfferRequest createOfferRequest, @RequestHeader("Authorization")String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoFirmAccountCanNotDoThatException {
        OfferDto newOfferDto = offerService.createOffer(createOfferRequest, jwt);
        return new ResponseEntity<>(newOfferDto, HttpStatus.OK);
    }

    @PutMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> updateOffer(@Valid @RequestBody CreateOfferRequest createOfferRequest, @PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        OfferDto updatedOfferDto = offerService.updateOffer(createOfferRequest, offerId, jwt);
        return new ResponseEntity<>(updatedOfferDto, HttpStatus.OK);
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable("offerId") Long offerId) throws OfferNotFoundByIdException {
        OfferDto offerDto = offerService.getOffer(offerId);
        return new ResponseEntity<>(offerDto, HttpStatus.OK);
    }

    @GetMapping("/offer/search")
    public ResponseEntity<PageResponse<OfferDto>> searchOffers(@RequestParam(defaultValue = "") String text, @RequestParam(required = false) List<String> jobLevels,
                                                            @RequestParam(required = false) List<String> operatingModes,
                                                            @RequestParam(required = false) List<String> localizations,
                                                               @RequestParam(required = false) List<String> specializations,
                                                            @RequestParam(required = false) List<Long> technologies,
                                                            @RequestParam String sortingDirection,
                                                            @RequestParam String sortBy, @RequestParam Integer pageNumber,
                                                            @RequestParam Integer numberOfElements){
        SearchOffersRequest searchOffersRequest = new SearchOffersRequest();
        searchOffersRequest.setText(text);
        searchOffersRequest.setLocalizations(localizations);
        searchOffersRequest.setJobLevels(jobLevels);
        searchOffersRequest.setSortBy(sortBy);
        searchOffersRequest.setSortingDirection(sortingDirection);
        searchOffersRequest.setPageNumber(pageNumber);
        searchOffersRequest.setNumberOfElements(numberOfElements);
        searchOffersRequest.setTechnologies(technologies);
        searchOffersRequest.setOperatingModes(operatingModes);
        searchOffersRequest.setSpecializations(specializations);
        PageResponse<OfferDto> result = offerService.searchOffer(searchOffersRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/offer/{offerId}")
    public ResponseEntity<MessageResponse> deleteOfferById(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        offerService.deleteOfferById(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Offer deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/like/{offerId}")
    public ResponseEntity<MessageResponse> likeOffer(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException,
            OfferIsAlreadyLikedByUserException,
            OfferNotFoundByIdException, NoPermissionException {
        offerService.likeOffer(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse("Liked the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/remove-like/{offerId}")
    public ResponseEntity<MessageResponse> removeLikeOffer(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException,
            OfferNotFoundByIdException, OfferIsNotLikedByUserException, NoPermissionException {
        offerService.removeLikeOffer(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse("Like has been removed");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/add-application-to-favourites")
    public ResponseEntity<MessageResponse> addApplicationToFavourites(@RequestParam("offerId") Long offerId,
                                                                      @RequestParam("applicationId") Long applicationId,
                                                                      @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, OfferNotFoundByIdException, ApplicationAlreadyIsInFavouritesException, UserNotFoundByJwtException, NoPermissionException {
        offerService.addApplicationToFavourites(offerId, applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been added to favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    @PostMapping("/offer/remove-application-from-favourites")
    public ResponseEntity<MessageResponse> removeApplicationFromFavourites(@RequestParam("offerId") Long offerId,
                                                                           @RequestParam("applicationId") Long applicationId,
                                                                           @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, OfferNotFoundByIdException, ApplicationIsNotInFavouritesException, UserNotFoundByJwtException, NoPermissionException {
        offerService.removeApplicationFromFavourites(offerId, applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been removed from favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/add-recruiter")
    public ResponseEntity<MessageResponse> addRecruiter(@RequestParam("offerId") Long offerId,
                                                        @RequestParam("userId") Long userId,
                                                        @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsAlreadyRecruiterException, NoPermissionException, UserNotFoundByJwtException {
        offerService.addRecruiterToOffer(offerId, userId, jwt);
        MessageResponse messageResponse = new MessageResponse("Recruiter has been added to the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/remove-recruiter")
    public ResponseEntity<MessageResponse> removeRecruiter(@RequestParam("offerId") Long offerId,
                                                           @RequestParam("userId") Long userId,
                                                           @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsNotRecruiterException, UserNotFoundByJwtException, NoPermissionException {
        offerService.removeRecruiterFromOffer(offerId, userId, jwt);
        MessageResponse messageResponse = new MessageResponse("Recruiter has been removed from the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("offer/liked")
    public ResponseEntity<PageResponse<OfferDto>> getLikedOffersFromJwt(@RequestParam("numberOfElements") Integer numberOfElements, @RequestParam("pageNumber") Integer pageNumber, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException {
        PageResponse<OfferDto> pageResponse = offerService.getLikedOffersFromJwt(numberOfElements, pageNumber, jwt);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("offer/from-recruiter/{recruiterId}")
    public ResponseEntity<PageResponse<OfferDto>> getOffersFromRecruiter(@PathVariable("recruiterId") Long recruiterId,
                                                                         @RequestParam(value = "isActive", required = false) Boolean isActive,
                                                                         @RequestParam("numberOfElements") Integer numberOfElements,
                                                                         @RequestParam("pageNumber") Integer pageNumber,
                                                                         @RequestParam("sortBy") String sortBy,
                                                                         @RequestParam("sortDirection") String sortDirection,
                                                                         @RequestHeader("Authorization") String jwt) throws UserNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        PageResponse<OfferDto> pageResponse = offerService.getOffersFromRecruiter(recruiterId, isActive, numberOfElements, pageNumber, sortBy, sortDirection, jwt);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
