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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
public class OfferController {

    private OfferService offerService;


    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/offer")
    public ResponseEntity<OfferDto> createOffer(@Valid @RequestBody CreateOfferRequest createOfferRequest, @RequestHeader("Authorization")String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NotFirmAccountCanNotDoThatException {
        OfferDto newOfferDto = offerService.createOffer(createOfferRequest, jwt);
        return new ResponseEntity<>(newOfferDto, HttpStatus.OK);
    }

    @PutMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> updateOffer(@Valid @RequestBody CreateOfferRequest createOfferRequest, @PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        OfferDto updatedOfferDto = offerService.updateOffer(createOfferRequest, offerId, jwt);
        return new ResponseEntity<>(updatedOfferDto, HttpStatus.OK);
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException {
        OfferDto offerDto = offerService.getOffer(offerId, jwt);
        return new ResponseEntity<>(offerDto, HttpStatus.OK);
    }

    @GetMapping("/offer/search")
    public ResponseEntity<PageResponse<OfferDto>> searchOffers(@RequestParam(defaultValue = "") String text, @RequestParam(required = false) List<String> jobLevels,
                                                            @RequestParam(required = false) List<String> operatingModes,
                                                            @RequestParam(required = false) List<String> localizations,
                                                            @RequestParam(required = false) List<Long> technologies,
                                                            @RequestParam String sortingDirection,
                                                            @RequestParam String sortBy, @RequestParam Integer pageNumber,
                                                            @RequestParam Integer numberOfElements, @RequestHeader("Authorization") String jwt){
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
        PageResponse<OfferDto> result = offerService.searchOffer(searchOffersRequest, jwt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/offer/{offerId}")
    public ResponseEntity<MessageResponse> deleteOfferById(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException {
        offerService.deleteOfferById(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Offer deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/likeOffer/{offerId}")
    public ResponseEntity<MessageResponse> likeOffer(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException,
            OfferIsAlreadyLikedByUserException,
            OfferNotFoundByIdException
    {
        offerService.likeOffer(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse("Liked the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/removeLikeOffer/{offerId}")
    public ResponseEntity<MessageResponse> removeLikeOffer(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException,
            OfferNotFoundByIdException, OfferIsNotLikedByUserException
    {
        offerService.removeLikeOffer(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse("Like has been removed");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/addApplicationToFavourites")
    public ResponseEntity<MessageResponse> addApplicationToFavourites(@RequestParam("offerId") Long offerId,
                                                                      @RequestParam("applicationId") Long applicationId,
                                                                      @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, OfferNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        offerService.addApplicationToFavourites(offerId, applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been added to favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    @PostMapping("/offer/removeApplicationFromFavourites")
    public ResponseEntity<MessageResponse> removeApplicationFromFavourites(@RequestParam("offerId") Long offerId,
                                                                           @RequestParam("applicationId") Long applicationId,
                                                                           @RequestHeader("Authorization") String jwt)
            throws ApplicationNotFoundByIdException, OfferNotFoundByIdException, ApplicationIsNotInFavouritesException {
        offerService.removeApplicationFromFavourites(offerId, applicationId, jwt);
        MessageResponse messageResponse = new MessageResponse("Application has been removed from favourites");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/addRecruiter")
    public ResponseEntity<MessageResponse> addRecruiter(@RequestParam("offerId") Long offerId,
                                                        @RequestParam("userId") Long userId,
                                                        @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsAlreadyRecruiterException {
        offerService.addRecruiterToOffer(offerId, userId, jwt);
        MessageResponse messageResponse = new MessageResponse("Recruiter has been added to the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PostMapping("/offer/removeRecruiter")
    public ResponseEntity<MessageResponse> removeRecruiter(@RequestParam("offerId") Long offerId,
                                                           @RequestParam("userId") Long userId,
                                                           @RequestHeader("Authorization") String jwt) throws OfferNotFoundByIdException, UserNotFoundByIdException, UserIsNotRecruiterException {
        offerService.removeRecruiterFromOffer(offerId, userId, jwt);
        MessageResponse messageResponse = new MessageResponse("Recruiter has been removed from the offer");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("offer/liked")
    public ResponseEntity<PageResponse<OfferDto>> getLikedOffersFromJwt(@RequestParam("numberOfElements") Integer numberOfElements, @RequestParam("pageNumber") Integer pageNumber, @RequestHeader("Authorization") String jwt) throws UserNotFoundByJwtException {
        PageResponse<OfferDto> pageResponse = offerService.getLikedOffersFromJwt(numberOfElements, pageNumber, jwt);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
