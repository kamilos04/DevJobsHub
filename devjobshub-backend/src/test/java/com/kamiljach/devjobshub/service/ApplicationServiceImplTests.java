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
import com.kamiljach.devjobshub.response.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

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
    private Application application1;
    private Application application2;

    private Offer offer1;
    private String validJwt;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        validJwt = "someJwt";
        offer1 = new Offer();
        offer1.setId(4L);
        application1 = new Application();
        application1.setCvUrl("test url");
        application1.setDateTimeOfCreation(LocalDateTime.parse("02-01-2024 23:59:59", Constants.dateTimeFormatter));
        application1.setOffer(offer1);
        application2 = new Application();
        application2.setCvUrl("test url2");
        application2.setDateTimeOfCreation(LocalDateTime.parse("02-01-2024 21:59:59", Constants.dateTimeFormatter));



    }

    @Test
    public void ApplicationService_applyForOffer_ReturnsApplicationDto() throws UserAlreadyAppliedForThisOfferException, QuestionOrAnswerIsIncorrectException, FirmAccountCanNotDoThatException, OfferExpiredException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
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


        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.getApplicationById(validApplicationId, validJwt));
    }


    @Test
    public void ApplicationService_getApplicationById_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        Application applicationA = mock(Application.class);

        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionGetApplicationById(user1, applicationA);

        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.getApplicationById(validApplicationId, validJwt));
    }

    @Test
    public void ApplicationService_deleteApplicationById_Success() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
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

        when(applicationRepository.findById(invalidApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.deleteApplicationById(invalidApplicationId, validJwt));

        verify(applicationServiceSpy, never()).deleteApplication(any());

    }

    @Test
    public void ApplicationService_deleteApplicationById_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException {
        Long validApplicationId = 1L;
        Application applicationA = mock(Application.class);

        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(applicationA));
        when(userService.findUserByJwt(validJwt)).thenReturn(user1);
        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionDeleteApplicationById(user1, applicationA);


        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.deleteApplicationById(validApplicationId, validJwt));

        verify(applicationServiceSpy, never()).deleteApplication(any());

    }


    @Test
    public void ApplicationService_deleteApplication_Success(){
        Offer offer = mock(Offer.class);
        User user = mock(User.class);
        Application application = mock(Application.class);

        when(application.getOffer()).thenReturn(offer);
        when(application.getUser()).thenReturn(user);


        applicationService.deleteApplication(application);

        verify(application).removeUser();
        verify(application).removeOffer();
        verify(offerRepository).save(offer);
        verify(userRepository).save(user);
        verify(applicationRepository).delete(application);
    }

    @Test
    public void ApplicationService_getApplicationsFromOffer_ReturnsPageResponse() throws NoPermissionException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        Offer offer = mock(Offer.class);
        User user = mock(User.class);

        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offer));
        when(userService.findUserByJwt(validJwt)).thenReturn(user);
        doNothing().when(applicationServiceSpy).validatePermissionGetApplicationsFromOffer(user, offer);

        Page<Application> page = new PageImpl<>(Arrays.asList(application1, application2));
        when(applicationRepository.getApplicationsFromOffer(eq(validOfferId), any(Pageable.class))).thenReturn(page);

        PageResponse<ApplicationDto> result = applicationServiceSpy.getApplicationsFromOffer(validOfferId, 10, 0, false, validJwt);
        assertNotNull(result);
        assertEquals(result.getTotalElements(), 2);
    }


    @Test
    public void ApplicationService_getApplicationsFromOffer_ThrowsOfferNotFoundByIdException() throws NoPermissionException, OfferNotFoundByIdException {
        Long invalidOfferId = 1L;

        when(offerRepository.findById(invalidOfferId)).thenReturn(Optional.empty());

        assertThrows(OfferNotFoundByIdException.class, () -> applicationServiceSpy.getApplicationsFromOffer(invalidOfferId, 10, 0, false, validJwt));

        verify(applicationRepository, never()).getApplicationsFromOffer(any(), any());
    }

    @Test
    public void ApplicationService_getApplicationsFromOffer_ThrowsNoPermissionException() throws NoPermissionException, OfferNotFoundByIdException {
        Long validOfferId = 1L;
        Offer offer = mock(Offer.class);
        User user = mock(User.class);

        when(offerRepository.findById(validOfferId)).thenReturn(Optional.of(offer));
        when(userService.findUserByJwt(validJwt)).thenReturn(user);
        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionGetApplicationsFromOffer(user, offer);


        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.getApplicationsFromOffer(validOfferId, 10, 0, false, validJwt));

        verify(applicationRepository, never()).getApplicationsFromOffer(any(), any());
    }


    @Test
    public void ApplicationService_addApplicationToFavourites_Success() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(false);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doNothing().when(applicationServiceSpy).validatePermissionAddApplicationToFavourites(eq(userA), any(Offer.class));

        applicationServiceSpy.addApplicationToFavourites(validApplicationId, validJwt);

        assertEquals(application1.getIsFavourite(), true);
        verify(applicationRepository).save(application1);

    }

    @Test
    public void ApplicationService_addApplicationToFavourites_ThrowsApplicationNotFoundByIdException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long invalidApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(false);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(invalidApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.addApplicationToFavourites(invalidApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), false);
        verify(applicationRepository, never()).save(application1);

    }


    @Test
    public void ApplicationService_addApplicationToFavourites_ThrowsApplicationAlreadyIsInFavouritesException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(true);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doNothing().when(applicationServiceSpy).validatePermissionAddApplicationToFavourites(eq(userA), any(Offer.class));


        assertThrows(ApplicationAlreadyIsInFavouritesException.class, () -> applicationServiceSpy.addApplicationToFavourites(validApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), true);
        verify(applicationRepository, never()).save(application1);

    }

    @Test
    public void ApplicationService_addApplicationToFavourites_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(false);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionAddApplicationToFavourites(eq(userA), any(Offer.class));


        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.addApplicationToFavourites(validApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), false);
        verify(applicationRepository, never()).save(application1);

    }

    @Test
    public void ApplicationService_removeApplicationFromFavourites_Success() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationIsNotInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(true);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doNothing().when(applicationServiceSpy).validatePermissionRemoveApplicationFromFavourites(eq(userA), any(Offer.class));

        applicationServiceSpy.removeApplicationFromFavourites(validApplicationId, validJwt);

        assertEquals(application1.getIsFavourite(), false);
        verify(applicationRepository).save(application1);

    }

    @Test
    public void ApplicationService_removeApplicationFromFavourites_ThrowsApplicationNotFoundByIdException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long invalidApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(true);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(invalidApplicationId)).thenReturn(Optional.empty());


        assertThrows(ApplicationNotFoundByIdException.class, () -> applicationServiceSpy.removeApplicationFromFavourites(invalidApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), true);
        verify(applicationRepository, never()).save(application1);

    }


    @Test
    public void ApplicationService_removeApplicationFromFavourites_ThrowsApplicationIsNotInFavouritesException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(false);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doNothing().when(applicationServiceSpy).validatePermissionRemoveApplicationFromFavourites(eq(userA), any(Offer.class));


        assertThrows(ApplicationIsNotInFavouritesException.class, () -> applicationServiceSpy.removeApplicationFromFavourites(validApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), false);
        verify(applicationRepository, never()).save(application1);

    }

    @Test
    public void ApplicationService_removeApplicationFromFavourites_ThrowsNoPermissionException() throws NoPermissionException, ApplicationNotFoundByIdException, ApplicationAlreadyIsInFavouritesException {
        Long validApplicationId = 2L;
        String validJwt = "some jwt";
        User userA = mock(User.class);
        application1.setIsFavourite(true);

        when(userService.findUserByJwt(validJwt)).thenReturn(userA);
        when(applicationRepository.findById(validApplicationId)).thenReturn(Optional.of(application1));

        doThrow(NoPermissionException.class).when(applicationServiceSpy).validatePermissionRemoveApplicationFromFavourites(eq(userA), any(Offer.class));


        assertThrows(NoPermissionException.class, () -> applicationServiceSpy.removeApplicationFromFavourites(validApplicationId, validJwt));

        assertEquals(application1.getIsFavourite(), true);
        verify(applicationRepository, never()).save(application1);

    }



}
