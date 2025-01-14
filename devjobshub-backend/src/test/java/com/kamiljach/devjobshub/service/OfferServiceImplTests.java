package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kamiljach.devjobshub.TestDataUtil;
import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.NoFirmAccountCanNotDoThatException;
import com.kamiljach.devjobshub.exceptions.exceptions.TechnologyNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByIdException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
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

    @Mock
    private UserService userService;

    @InjectMocks
    private OfferServiceImpl offerService;


    private CreateOfferRequest validRequest;
    private Technology technology1;
    private Technology technology2;

    private User testUserA;
    private User testUserAdminA;

    private Offer offer1;

    @BeforeEach
    void setUp(){
        validRequest = new CreateOfferRequest();
        validRequest.setName("Test offer");
        validRequest.setExpirationDate("02-02-2026 23:59:59");
        technology1 = new Technology();
        technology2 = new Technology();
        offer1 = new Offer();
        testUserA = TestDataUtil.createTestUserA();
        testUserAdminA = TestDataUtil.createTestUserAdminA();


    }

    @Test
    public void OfferService_createOffer_ReturnsOfferDto() throws UserNotFoundByJwtException, TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException {
        String validJwt = "some jwt";
        Technology technologyA = mock(Technology.class);
        Technology technologyB = mock(Technology.class);
        User userA = mock(User.class);
        when(userService.findUserByJwt(validJwt)).thenReturn(userA);


        validRequest.setRequiredTechnologies(new ArrayList<Long>(Arrays.asList(1L,2L)));
        validRequest.setNiceToHaveTechnologies(new ArrayList<Long>(Arrays.asList(1L)));
        when(utilityService.getListOfTechnologiesFromTheirIds(eq(new ArrayList<Long>(Arrays.asList(1L,2L))))).thenReturn(new ArrayList<>(Arrays.asList(technologyA, technologyB)));
        when(utilityService.getListOfTechnologiesFromTheirIds(eq(new ArrayList<Long>(Arrays.asList(1L))))).thenReturn(new ArrayList<>(Arrays.asList(technologyA)));

        OfferDto result = offerService.createOffer(validRequest, validJwt);

        assertNotNull(result);
        assertEquals(result.getName(), validRequest.getName());
        verify(userRepository, times(1)).save(Mockito.any());
        verify(technologyRepository, times(3)).save(any());
        verify(offerRepository, times(1)).save(any());

    }


    @Test
    public void OfferService_createOffer_ThrowsNoFirmAccountCanNotDoThatException() throws UserNotFoundByJwtException, TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException {
        String validJwt = "some jwt";

        User userA = mock(User.class);
        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        doThrow(NoFirmAccountCanNotDoThatException.class).when(utilityService).isFirmOrThrowException(any(User.class));


        assertThrows(NoFirmAccountCanNotDoThatException.class, () -> offerService.createOffer(validRequest, validJwt));

        verify(userRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());
        verify(offerRepository, never()).save(any());


    }


    @Test
    public void OfferService_createOffer_ThrowsUserNotFoundByJwtException() throws UserNotFoundByJwtException, TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException {
        String incorrectJwt = "some jwt";

        doThrow(UserNotFoundByJwtException.class).when(userService).findUserByJwt(eq(incorrectJwt));


        assertThrows(UserNotFoundByJwtException.class, () -> offerService.createOffer(validRequest, incorrectJwt));


        verify(userRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());
        verify(offerRepository, never()).save(any());

    }


    @Test
    public void OfferService_createOffer_ThrowsTechnologyNotFoundByIdException() throws UserNotFoundByJwtException, TechnologyNotFoundByIdException, NoFirmAccountCanNotDoThatException {
        String validJwt = "some jwt";
        Technology technologyA = mock(Technology.class);
        Technology technologyB = mock(Technology.class);
        User userA = mock(User.class);
        when(userService.findUserByJwt(validJwt)).thenReturn(userA);


        validRequest.setRequiredTechnologies(new ArrayList<Long>(Arrays.asList(1L,2L)));
        validRequest.setNiceToHaveTechnologies(new ArrayList<Long>(Arrays.asList(1L)));
        doThrow(TechnologyNotFoundByIdException.class).when(utilityService).getListOfTechnologiesFromTheirIds(any());


        assertThrows(TechnologyNotFoundByIdException.class, () -> offerService.createOffer(validRequest, validJwt));


        verify(offerRepository, never()).save(any());

    }









}
