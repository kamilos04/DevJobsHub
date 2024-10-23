package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.impl.TechnologyServiceImpl;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class TechnologyServiceImplTests {
    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private OfferRepository offerRepository;


    private CreateTechnologyRequest validRequests;

    @InjectMocks
    private TechnologyServiceImpl technologyService;

    @BeforeEach
    void setUp(){
        validRequests = new CreateTechnologyRequest();
        validRequests.setName("New Technology");
    }

    @Test
    public void TechnologyService_createTechnology_Success() throws TechnologyWithThisNameAlreadyExistsException, OfferNotFoundByIdException {
        when(technologyRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());

        TechnologyDto result = technologyService.createTechnology(validRequests, "some jwt");

        assertNotNull(result);
        assertEquals(result.getName(), validRequests.getName());
        verify(technologyRepository, times(1)).save(Mockito.any(Technology.class));

    }

    @Test
    public void TechnologyService_createTechnology_ThrowsTechnologyWithThisNameAlreadyExistsException(){
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(technologyRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(existingTechnology));

        assertThrows(TechnologyWithThisNameAlreadyExistsException.class, () -> {
            technologyService.createTechnology(validRequests, "some jwt");}
        );
        verify(technologyRepository, never()).save(any(Technology.class));
    }

    @Test
    public void TechnologyService_deleteTechnologyById_Success() throws TechnologyNotFoundByIdException {
        TechnologyServiceImpl technologyServiceSpy = Mockito.spy(technologyService);

        Long validId = 1L;
        String validJwt = "some jwt";
        Technology technologyToDelete = new Technology();
        technologyToDelete.setId(validId);

        Offer offer1 = mock(Offer.class);
        Offer offer2 = mock(Offer.class);
        Offer offer3 = mock(Offer.class);

        technologyToDelete.setAssignedAsNiceToHave(new ArrayList<>(List.of(offer1, offer2)));
        technologyToDelete.setAssignedAsRequired(new ArrayList<>(List.of(offer1, offer2, offer3)));


        when(technologyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(technologyToDelete));


        technologyServiceSpy.deleteTechnologyById(validId, "some jwt");


        verify(technologyRepository, times(1)).delete(Mockito.any(Technology.class));
        verify(technologyServiceSpy, times(2)).deleteOfferFromAssignedAsNiceToHave(eq(technologyToDelete), Mockito.any(Offer.class));
        verify(technologyServiceSpy, times(3)).deleteOfferFromAssignedAsRequired(eq(technologyToDelete), Mockito.any(Offer.class));
    }

    @Test
    public void TechnologyService_deleteTechnologyById_ThrowsTechnologyNotFoundByIdException(){
        Long validId = 1L;
        String validJwt = "some jwt";

        when(technologyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TechnologyNotFoundByIdException.class, () -> {
            technologyService.deleteTechnologyById(validId, validJwt);
        });
    }

    @Test
    public void TechnologyService_updateTechnology_ReturnsTechnologyDto() throws TechnologyNotFoundByIdException{
        Long validId = 1L;
        String validJwt = "some jwt";
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(technologyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(existingTechnology));

        TechnologyDto result = technologyService.updateTechnology(validRequests, validId, validJwt);


        assertNotNull(result);
        assertNotEquals(result.getName(), "Existing");
        verify(technologyRepository, times(1)).save(Mockito.any(Technology.class));

    }

    @Test
    public void TechnologyService_updateTechnology_ThrowsTechnologyNotFoundByIdException() throws TechnologyNotFoundByIdException {
        Long validId = 1L;
        String validJwt = "some jwt";

        when(technologyRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotFoundByIdException.class, () -> {
            technologyService.updateTechnology(validRequests, validId, validJwt);
        });

        verify(technologyRepository, never()).save(Mockito.any(Technology.class));
    }

    @Test
    public void TechnologyService_getTechnologyById_ReturnsTechnologyDto() throws TechnologyNotFoundByIdException {
        Long validId = 1L;
        String validJwt = "some jwt";
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(technologyRepository.findById(validId)).thenReturn(Optional.of(existingTechnology));

        TechnologyDto result = technologyService.getTechnologyById(validId, validJwt);

        assertNotNull(result);
    }

    @Test
    public void TechnologyService_getTechnologyById_ThrowsTechnologyNotFoundByIdException(){
        Long validId = 1L;
        String validJwt = "some jwt";

        when(technologyRepository.findById(validId)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotFoundByIdException.class, () -> {
            technologyService.getTechnologyById(validId, validJwt);
        });
    }

    @Test
    public void TechnologyService_addAssignedAsNiceHave_Success(){
        Technology technology = new Technology();
        Offer offer = new Offer();
        assertFalse(technology.getAssignedAsNiceToHave().contains(offer));

        technologyService.addAssignedAsNiceToHave(technology, offer);

        verify(technologyRepository, times(1)).save(technology);
        verify(offerRepository, times(1)).save(offer);
        assertTrue(technology.getAssignedAsNiceToHave().contains(offer));
        assertTrue(offer.getNiceToHaveTechnologies().contains(technology));

    }

    @Test
    public void TechnologyService_addAssignedAsRequired_Success(){
        Technology technology = new Technology();
        Offer offer = new Offer();
        assertFalse(technology.getAssignedAsRequired().contains(offer));

        technologyService.addAssignedAsRequired(technology, offer);

        verify(technologyRepository, times(1)).save(technology);
        verify(offerRepository, times(1)).save(offer);
        assertTrue(technology.getAssignedAsRequired().contains(offer));
        assertTrue(offer.getRequiredTechnologies().contains(technology));

    }

    @Test
    public void TechnologyService_deleteOfferFromAssignedAsRequired_Success(){
        Technology technology = new Technology();
        Offer offer = new Offer();

        technologyService.addAssignedAsRequired(technology, offer);

        assertTrue(technology.getAssignedAsRequired().contains(offer));

        technologyService.deleteOfferFromAssignedAsRequired(technology, offer);

        assertFalse(technology.getAssignedAsRequired().contains(offer));
        assertFalse(offer.getRequiredTechnologies().contains(technology));
    }

    @Test
    public void TechnologyService_deleteOfferFromAssignedAsNiceToHace_Success(){
        Technology technology = new Technology();
        Offer offer = new Offer();

        technologyService.addAssignedAsNiceToHave(technology, offer);

        assertTrue(technology.getAssignedAsNiceToHave().contains(offer));

        technologyService.deleteOfferFromAssignedAsNiceToHave(technology, offer);

        assertFalse(technology.getAssignedAsNiceToHave().contains(offer));
        assertFalse(offer.getNiceToHaveTechnologies().contains(technology));
    }




}
