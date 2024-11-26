package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.response.MessageResponse;
import com.kamiljach.devjobshub.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<OfferDto> createOffer(@RequestBody CreateOfferRequest createOfferRequest, @RequestHeader("Authorization")String jwt) throws TechnologyNotFoundByIdException {
        OfferDto newOfferDto = offerService.createOffer(createOfferRequest, jwt);
        return new ResponseEntity<>(newOfferDto, HttpStatus.OK);
    }

    @PutMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> updateOffer(@RequestBody CreateOfferRequest createOfferRequest, @PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        OfferDto updatedOfferDto = offerService.updateOffer(createOfferRequest, offerId, jwt);
        return new ResponseEntity<>(updatedOfferDto, HttpStatus.OK);
    }

    @GetMapping("/offer/{offerId}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException {
        OfferDto offerDto = offerService.getOffer(offerId, jwt);
        return new ResponseEntity<>(offerDto, HttpStatus.OK);
    }

    @GetMapping("/offer/search")
    public ResponseEntity<ArrayList<OfferDto>> searchOffers(@RequestParam(defaultValue = "") String text, @RequestParam(required = false) List<String> jobLevels,
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
        ArrayList<OfferDto> result = offerService.searchOffer(searchOffersRequest, jwt);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/offer/{offerId}")
    public ResponseEntity<MessageResponse> deleteOfferById(@PathVariable("offerId") Long offerId, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException {
        offerService.deleteOfferById(offerId, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Offer deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


}
