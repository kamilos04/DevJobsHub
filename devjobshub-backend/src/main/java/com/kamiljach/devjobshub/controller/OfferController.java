package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
}
