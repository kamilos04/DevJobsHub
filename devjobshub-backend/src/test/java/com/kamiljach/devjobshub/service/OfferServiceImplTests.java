package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.service.impl.OfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class OfferServiceImplTests {
    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private OfferServiceImpl offerService;


    private CreateOfferRequest validRequest;
    private Technology technology1;
    private Technology technology2;


    @BeforeEach
    void setUp(){
        validRequest = new CreateOfferRequest();
        validRequest.setRequiredTechnologies(new ArrayList<>(Arrays.asList(0L, 1L)));
        technology1 = new Technology();
        technology2 = new Technology();

    }

    @Test
    public void OfferService_createOffer_Success() throws TechnologyNotFoundByIdException {
        String validJwt = "some jwt";

        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getRequiredTechnologies())).thenReturn(new ArrayList<Technology>(Arrays.asList(technology1, technology2)));
        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getNiceToHaveTechnologies())).thenReturn(new ArrayList<>(Arrays.asList(technology1)));

        OfferDto result = offerService.createOffer(validRequest, validJwt);

        assertNotNull(result);
        verify(offerRepository, times(4)).save(Mockito.any(Offer.class));
        assertEquals(result.getRequiredTechnologies().size(), 2);
        assertEquals(result.getNiceToHaveTechnologies().size(), 1);
        assertNotNull(result.getDateTimeOfCreation());

    }

}
