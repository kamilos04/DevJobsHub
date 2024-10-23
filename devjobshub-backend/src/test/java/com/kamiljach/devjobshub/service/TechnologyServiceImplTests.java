package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyWithThisNameAlreadyExistsException;
import com.kamiljach.devjobshub.model.Technology;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TechnologyServiceImplTests {
    @Mock
    private TechnologyRepository technologyRepository;


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
    public void TechnologyService_createTechnology_ThrowsAlreadyExistsException(){
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(technologyRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(existingTechnology));

        assertThrows(TechnologyWithThisNameAlreadyExistsException.class, () -> {
            technologyService.createTechnology(validRequests, "some jwt");}
        );
        verify(technologyRepository, never()).save(any(Technology.class));
    }
}
