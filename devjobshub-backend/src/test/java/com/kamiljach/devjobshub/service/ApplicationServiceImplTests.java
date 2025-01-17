package com.kamiljach.devjobshub.service;

import com.kamiljach.devjobshub.config.Constants;
import com.kamiljach.devjobshub.dto.ApplicationDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.model.embeddable.QuestionAndAnswer;
import com.kamiljach.devjobshub.repository.ApplicationRepository;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.application.CreateApplicationRequest;
import com.kamiljach.devjobshub.service.impl.ApplicationServiceImpl;
import org.assertj.core.condition.MappedCondition;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceImplTests {
    @Mock
    private OfferRepository offerRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private UtilityService utilityService;


    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Spy
    @InjectMocks
    private ApplicationServiceImpl applicationServiceSpy;

    private User user1;

    @BeforeEach
    void setUp() {
        User user1 = new User();
    }

    @Test
    public void ApplicationService_applyForOffer_ReturnsApplicationDto() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest validRequest = mock(CreateApplicationRequest.class);
        Offer offerA = mock(Offer.class);
        Application applicationA = mock(Application.class);
        ApplicationDto applicationDtoA = mock(ApplicationDto.class);


        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offerA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);

        doNothing().when(applicationServiceSpy).ifUserAlreadyAppliedForOfferThrowException(user1, offerA);
        when(offerA.getExpirationDate()).thenReturn(LocalDateTime.parse("17-01-2026 18:13:44", Constants.dateTimeFormatter));
        when(validRequest.mapToApplication()).thenReturn(applicationA);
        doNothing().when(applicationServiceSpy).validateAllQuestionsInApplication(any(Application.class), eq(offerA));
        when(applicationA.mapToApplicationDtoShallow()).thenReturn(applicationDtoA);

        ApplicationDto result = applicationServiceSpy.applyForOffer(validRequest, validOfferId, validJwt);

        assertNotNull(result);
        verify(userRepository).save(user1);
        verify(offerRepository).save(offerA);
        verify(applicationRepository).save(any(Application.class));

    }

    @Test
    public void ApplicationService_applyForOffer_ThrowsOfferNotFoundByIdException() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long invalidOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest validRequest = mock(CreateApplicationRequest.class);



        when(offerRepository.findById(invalidOfferId)).thenReturn(Optional.empty());

        assertThrows(OfferNotFoundByIdException.class, () -> applicationServiceSpy.applyForOffer(validRequest, invalidOfferId, validJwt));

        verify(userRepository, never()).save(any());
        verify(offerRepository, never()).save(any());
        verify(applicationRepository, never()).save(any());

    }

    @Test
    public void ApplicationService_applyForOffer_ThrowsQuestionOrAnswerIsIncorrectException() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest invalidRequest = mock(CreateApplicationRequest.class);
        Offer offerA = mock(Offer.class);
        Application applicationA = mock(Application.class);


        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offerA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);

        doNothing().when(applicationServiceSpy).ifUserAlreadyAppliedForOfferThrowException(user1, offerA);
        when(offerA.getExpirationDate()).thenReturn(LocalDateTime.parse("17-01-2026 18:13:44", Constants.dateTimeFormatter));
        when(invalidRequest.mapToApplication()).thenReturn(applicationA);
        doThrow(QuestionOrAnswerIsIncorrectException.class).when(applicationServiceSpy).validateAllQuestionsInApplication(any(Application.class), eq(offerA));

        assertThrows(QuestionOrAnswerIsIncorrectException.class, () -> applicationServiceSpy.applyForOffer(invalidRequest, validOfferId, validJwt));

        verify(userRepository, never()).save(any());
        verify(offerRepository, never()).save(any());
        verify(applicationRepository, never()).save(any());

    }


    @Test
    public void ApplicationService_applyForOffer_ThrowsOfferExpiredException() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest validRequest = mock(CreateApplicationRequest.class);
        Offer offerA = mock(Offer.class);


        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offerA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);

        doNothing().when(applicationServiceSpy).ifUserAlreadyAppliedForOfferThrowException(user1, offerA);
        when(offerA.getExpirationDate()).thenReturn(LocalDateTime.parse("17-01-2024 18:13:44", Constants.dateTimeFormatter));


        assertThrows(OfferExpiredException.class, () -> applicationServiceSpy.applyForOffer(validRequest, validOfferId, validJwt));

        verify(userRepository, never()).save(any());
        verify(offerRepository, never()).save(any());
        verify(applicationRepository, never()).save(any());

    }

    @Test
    public void ApplicationService_applyForOffer_ThrowsFirmAccountCanNotDoThatException() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest validRequest = mock(CreateApplicationRequest.class);
        Offer offerA = mock(Offer.class);


        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offerA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doThrow(FirmAccountCanNotDoThatException.class).when(utilityService).isFirmFalseOrThrowException(user1);


        assertThrows(FirmAccountCanNotDoThatException.class, () -> applicationServiceSpy.applyForOffer(validRequest, validOfferId, validJwt));

        verify(userRepository, never()).save(any());
        verify(offerRepository, never()).save(any());
        verify(applicationRepository, never()).save(any());

    }

    @Test
    public void ApplicationService_applyForOffer_ThrowsUserAlreadyAppliedForThisOfferException() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        String validJwt = "some jwt";
        CreateApplicationRequest validRequest = mock(CreateApplicationRequest.class);
        Offer offerA = mock(Offer.class);


        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offerA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);

        doThrow(UserAlreadyAppliedForThisOfferException.class).when(applicationServiceSpy).ifUserAlreadyAppliedForOfferThrowException(user1, offerA);


        assertThrows(UserAlreadyAppliedForThisOfferException.class, () -> applicationServiceSpy.applyForOffer(validRequest, validOfferId, validJwt));

        verify(userRepository, never()).save(any());
        verify(offerRepository, never()).save(any());
        verify(applicationRepository, never()).save(any());

    }

    @Test
    public void ApplicationService_getApplicationById_ReturnsApplicationDto() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        String validJwt = "some jwt";
        Application applicationA = mock(Application.class);
        ApplicationDto applicationDtoA = mock(ApplicationDto.class);


        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doNothing().when(applicationServiceSpy).validatePermissionGetApplicationById(user1, applicationA);
        when(applicationA.mapToApplicationDtoShallow()).thenReturn(applicationDtoA);

        ApplicationDto result = applicationServiceSpy.getApplicationById(validApplicationId, validJwt);

        assertNotNull(result);
    }


    @Test
    public void ApplicationService_getApplicationById_ThrowsApplicationNotFoundByIdException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        String validJwt = "some jwt";


        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.getApplicationById(validApplicationId, validJwt));
    }


    @Test
    public void ApplicationService_getApplicationById_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        String validJwt = "some jwt";
        Application applicationA = mock(Application.class);

        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionGetApplicationById(user1, applicationA);

        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.getApplicationById(validApplicationId, validJwt));
    }

    @Test
    public void ApplicationService_deleteApplicationById_Success() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        String validJwt = "some jwt";
        Application applicationA = mock(Application.class);

        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doNothing().when(applicationServiceSpy).validatePermissionDeleteApplicationById(user1, applicationA);

        doNothing().when(applicationServiceSpy).deleteApplication(applicationA);

        applicationServiceSpy.deleteApplicationById(validApplicationId, validJwt);

        verify(applicationServiceSpy).deleteApplication(applicationA);

    }

    @Test
    public void ApplicationService_deleteApplicationById_ThrowsApplicationNotFoundByIdException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long invalidApplicationId = 1L;
        String validJwt = "some jwt";

        when(applicationRepository.findById(invalidApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.deleteApplicationById(invalidApplicationId, validJwt));

        verify(applicationServiceSpy, never()).deleteApplication(any());

    }

    @Test
    public void ApplicationService_deleteApplicationById_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        String validJwt = "some jwt";
        Application applicationA = mock(Application.class);

        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionDeleteApplicationById(user1, applicationA);


        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.deleteApplicationById(validApplicationId, validJwt));

        verify(applicationServiceSpy, never()).deleteApplication(any());

    }


}
