package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.OfferNotFoundByIdException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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

    private Offer offer1;

    @BeforeEach
    void setUp(){
        validRequest = new CreateOfferRequest();
        technology1 = new Technology();
        technology2 = new Technology();
        offer1 = new Offer();

    }

//    @Test
//    public void OfferService_createOffer_Success() throws TechnologyNotFoundByIdException {
//        validRequest.setRequiredTechnologies(new ArrayList<>(Arrays.asList(0L, 1L)));
//        validRequest.setNiceToHaveTechnologies(new ArrayList<>(Arrays.asList(0L)));
//
//        String validJwt = "some jwt";
//
//        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getRequiredTechnologies())).thenReturn(new ArrayList<Technology>(Arrays.asList(technology1, technology2)));
//        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getNiceToHaveTechnologies())).thenReturn(new ArrayList<>(Arrays.asList(technology1)));
//
//        OfferDto result = offerService.createOffer(validRequest, validJwt);
//
//        assertNotNull(result);
//        verify(offerRepository, times(4)).save(Mockito.any(Offer.class));
//        assertEquals(result.getRequiredTechnologies().size(), 2);
//        assertEquals(result.getNiceToHaveTechnologies().size(), 1);
//        assertNotNull(result.getDateTimeOfCreation());
//
//    }
//
//    @Test
//    public void OfferService_updateOffer_Success() throws OfferNotFoundByIdException, TechnologyNotFoundByIdException {
//        validRequest.setRequiredTechnologies(new ArrayList<>(Arrays.asList(0L, 1L)));
//        validRequest.setNiceToHaveTechnologies(new ArrayList<>(Arrays.asList(0L)));
//        validRequest.setMinSalary(4000L);
//        offer1.setDateTimeOfCreation(LocalDateTime.now());
//        offer1.setNiceToHaveTechnologies(new ArrayList<>(Arrays.asList(technology1)));
//        offer1.setRequiredTechnologies(new ArrayList<>(Arrays.asList(technology2)));
//        offer1.setMinSalary(3000L);
//        technology1.setAssignedAsRequired(new ArrayList<>(Arrays.asList(offer1)));
//        technology1.setAssignedAsNiceToHave(new ArrayList<>(Arrays.asList(offer1)));
//        String validJwt = "some jwt";
//
//        Long validId = 1L;
//        when(offerRepository.findById(validId)).thenReturn(Optional.of(offer1));
//        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getRequiredTechnologies())).thenReturn(new ArrayList<>(Arrays.asList(technology1, technology2)));
//        when(utilityService.getListOfTechnologiesFromTheirIds(validRequest.getNiceToHaveTechnologies())).thenReturn(new ArrayList<>(Arrays.asList(technology1)));
//
//
//        OfferDto result = offerService.updateOffer(validRequest, validId, validJwt);
//
//        assertNotNull(result);
//        assertEquals(result.getNiceToHaveTechnologies().size(), 1);
//        assertEquals(result.getRequiredTechnologies().size(), 2);
//        assertEquals(result.getMinSalary(), 4000);
//        verify(utilityService, times(2)).getListOfTechnologiesFromTheirIds(Mockito.any());
//        verify(offerRepository, times(6)).save(Mockito.any(Offer.class));
//
//
//
//    }

}
