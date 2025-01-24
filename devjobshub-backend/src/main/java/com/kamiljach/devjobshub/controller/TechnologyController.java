package com.kamiljach.devjobshub.controller;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.response.MessageResponse;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.TechnologyService;
import jakarta.validation.Valid;
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
    public ResponseEntity<TechnologyDto> createTechnology(@Valid @RequestBody CreateTechnologyRequest createTechnologyRequest, @RequestHeader("Authorization")String jwt) throws OfferNotFoundByIdException, TechnologyWithThisNameAlreadyExistsException {
        TechnologyDto createdTechnologyDto = technologyService.createTechnology(createTechnologyRequest, jwt);
        return new ResponseEntity<>(createdTechnologyDto, HttpStatus.OK);
    }

    @DeleteMapping("/technology/{id}")
    public ResponseEntity<MessageResponse> deleteTechnologyById(@PathVariable("id") Long id, @RequestHeader("Authorization")String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        technologyService.deleteTechnologyById(id, jwt);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Technology deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @GetMapping("/technology/{id}")
    public ResponseEntity<TechnologyDto> getTechnologyById(@PathVariable("id") Long id, @RequestHeader("Authorization") String jwt) throws TechnologyNotFoundByIdException {
        return new ResponseEntity<>(technologyService.getTechnologyById(id, jwt), HttpStatus.OK);
    }


    @PutMapping("/technology/{id}")
    public ResponseEntity<TechnologyDto> updateTechnologyById(@Valid @PathVariable("id") Long id, @RequestBody CreateTechnologyRequest createTechnologyRequest, @RequestHeader("Authorization") String jwt) throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        TechnologyDto technologyDto = technologyService.updateTechnology(createTechnologyRequest, id, jwt);
        return new ResponseEntity<>(technologyDto, HttpStatus.OK);
    }

    @GetMapping("/technology/search")
    public ResponseEntity<PageResponse<TechnologyDto>> searchTechnologies(@RequestParam("text") String text){
        return new ResponseEntity<>(technologyService.searchTechnologies(text), HttpStatus.OK);
    }


}
