package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.TechnologyNotFoundByNameException;
import com.kamiljach.devjobshub.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.response.MessageResponse;
import com.kamiljach.devjobshub.service.TechnologyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TechnologyController {
    private TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping("/technology")
    public ResponseEntity<TechnologyDto> createTechnology(@RequestBody CreateTechnologyRequest createTechnologyRequest, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException {
        TechnologyDto createdTechnologyDto = technologyService.createTechnology(createTechnologyRequest.getName(), createTechnologyRequest.getAssignedAsRequiredIds(), createTechnologyRequest.getAssignedAsNiceToHaveIds(), jwt);
        return new ResponseEntity<>(createdTechnologyDto, HttpStatus.OK);
    }

    @DeleteMapping("/technology")
    public ResponseEntity<MessageResponse> deleteTechnologyByName(@RequestParam String name, @RequestHeader("Authorization")String jwt) throws TechnologyNotFoundByNameException {
        technologyService.deleteTechnologyByName(name, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Technology deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

}
