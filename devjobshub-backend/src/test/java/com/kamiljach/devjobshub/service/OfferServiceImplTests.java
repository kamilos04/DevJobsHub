package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kamiljach.devjobshub.TestDataUtil;
import com.kamiljach.devjobshub.config.Constants;
import com.kamiljach.devjobshub.dto.OfferDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.offer.CreateOfferRequest;
import com.kamiljach.devjobshub.request.offer.SearchOffersRequest;
import com.kamiljach.devjobshub.response.PageResponse;
import com.kamiljach.devjobshub.service.impl.OfferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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

    @Mock
    private UserService userService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Spy
    @InjectMocks
    private OfferServiceImpl offerServiceSpy;


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
        technology1.setId(10L);
        technology1.setId(12L);
        technology2 = new Technology();
        offer1 = new Offer();
        offer1.setDateTimeOfCreation(LocalDateTime.parse("02-01-2026 23:59:59", Constants.dateTimeFormatter));
        offer1.setExpirationDate(LocalDateTime.parse("02-03-2026 23:59:59", Constants.dateTimeFormatter));
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

    @Test
    public void OfferService_updateOffer_ReturnsOfferDto() throws UserNotFoundByJwtException, NoPermissionException, OfferNotFoundByIdException, TechnologyNotFoundByIdException {

        String validJwt = "some jwt";
        Long validId = 1L;
        Technology technologyA = mock(Technology.class);
        Technology technologyB = mock(Technology.class);
        User userA = mock(User.class);
        offer1.addRequiredTechnology(technology1);
        offer1.addRequiredTechnology(technology2);
        offer1.addNiceToHaveTechnology(technology1);

        doNothing().when(offerServiceSpy).validatePermissionUpdateOffer(userA, offer1);
        when(offerRepository.findById(validId)).thenReturn(Optional.of(offer1));

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);


        validRequest.setRequiredTechnologies(new ArrayList<Long>(Arrays.asList(1L,2L)));
        validRequest.setNiceToHaveTechnologies(new ArrayList<Long>(Arrays.asList(1L)));
        when(utilityService.getListOfTechnologiesFromTheirIds(eq(new ArrayList<Long>(Arrays.asList(1L,2L))))).thenReturn(new ArrayList<>(Arrays.asList(technologyA, technologyB)));
        when(utilityService.getListOfTechnologiesFromTheirIds(eq(new ArrayList<Long>(Arrays.asList(1L))))).thenReturn(new ArrayList<>(Arrays.asList(technologyA)));

        OfferDto result = offerServiceSpy.updateOffer(validRequest, validId, validJwt);

        assertNotNull(result);
        assertEquals(result.getName(), validRequest.getName());
        verify(technologyRepository, times(3+3)).save(any());
        verify(offerRepository, times(1)).save(any());
    }


    @Test
    public void OfferService_updateOffer_ThrowsOfferNotFoundException() throws UserNotFoundByJwtException, NoPermissionException, OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        String validJwt = "some jwt";
        Long incorrectId = 1L;
        when(offerRepository.findById(incorrectId)).thenReturn(Optional.empty());

        assertThrows(OfferNotFoundByIdException.class, () -> offerService.updateOffer(validRequest, incorrectId, validJwt));
        verify(offerRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());

    }


    @Test
    public void OfferService_updateOffer_ThrowsTechnologyNotFoundByIdException() throws UserNotFoundByJwtException, NoPermissionException, OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        String validJwt = "some jwt";
        Long validId = 1L;
        User userA = mock(User.class);

        doNothing().when(offerServiceSpy).validatePermissionUpdateOffer(userA, offer1);
        when(offerRepository.findById(validId)).thenReturn(Optional.of(offer1));

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);

        doThrow(TechnologyNotFoundByIdException.class).when(utilityService).getListOfTechnologiesFromTheirIds(any());

        assertThrows(TechnologyNotFoundByIdException.class, () -> offerServiceSpy.updateOffer(validRequest, validId, validJwt));
        verify(offerRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());


    }

    @Test
    public void OfferService_updateOffer_ThrowsUserNotFoundByJwtException() throws UserNotFoundByJwtException, NoPermissionException, OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        String invalidJwt = "some jwt";
        Long validId = 1L;

        when(offerRepository.findById(validId)).thenReturn(Optional.of(offer1));

        when(userService.findUserByJwt(invalidJwt)).thenThrow(UserNotFoundByJwtException.class);

        assertThrows(UserNotFoundByJwtException.class, () -> offerService.updateOffer(validRequest, validId, invalidJwt));
        verify(offerRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());


    }

    @Test
    public void OfferService_updateOffer_ThrowsNoPermissionException() throws UserNotFoundByJwtException, NoPermissionException, OfferNotFoundByIdException, TechnologyNotFoundByIdException {
        String validJwt = "some jwt";
        Long validId = 1L;
        User userA = mock(User.class);
        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(offerRepository.findById(validId)).thenReturn(Optional.of(offer1));
        doThrow(NoPermissionException.class).when(offerServiceSpy).validatePermissionUpdateOffer(userA, offer1);


        assertThrows(NoPermissionException.class, () -> offerServiceSpy.updateOffer(validRequest, validId, validJwt));
        verify(offerRepository, never()).save(any());
        verify(technologyRepository, never()).save(any());


    }


    @Test
    public void OfferService_searchOffer_ReturnsPageResponse() {
        SearchOffersRequest searchOffersRequest = new SearchOffersRequest();
        searchOffersRequest.setText("test");
        searchOffersRequest.setSortBy("name");
        searchOffersRequest.setPageNumber(1);
        searchOffersRequest.setNumberOfElements(1);
        searchOffersRequest.setLocalizations(Arrays.asList("warszawa"));
        searchOffersRequest.setJobLevels(Arrays.asList("SENIOR"));
        searchOffersRequest.setOperatingModes(Arrays.asList("STATIONARY"));
        searchOffersRequest.setSortingDirection("dsc");
        String validJwt = "some jwt";
        Page<Offer> page = new PageImpl<>(Arrays.asList(offer1));
        when(offerRepository.searchOffers(any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

        PageResponse<OfferDto> result = offerService.searchOffer(searchOffersRequest, validJwt);

        assertNotNull(result);
        assertEquals(result.getContent().getFirst().getName(), offer1.getName());
        assertEquals(result.getTotalElements(), 1);


    }














}
