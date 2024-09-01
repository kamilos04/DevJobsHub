package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class OfferController {

    private OfferService offerService;


    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/offer")
    public ResponseEntity<OfferDto> createOffer(@RequestBody CreateOfferRequest createOfferRequest, @RequestHeader("Authorization")String jwt){
        OfferDto newOfferDto = offerService.createOffer(createOfferRequest, jwt);
        return new ResponseEntity<>(newOfferDto, HttpStatus.OK);
    }
}
