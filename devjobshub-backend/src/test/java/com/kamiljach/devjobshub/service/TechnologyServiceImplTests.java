package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.kamiljach.devjobshub.TestDataUtil;
import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.exceptions.exceptions.*;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.TechnologyRepository;
import com.kamiljach.devjobshub.request.technology.CreateTechnologyRequest;
import com.kamiljach.devjobshub.service.impl.TechnologyServiceImpl;
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

    @Mock
    private UserService userService;


    @Mock
    private UtilityService utilityService;

    private CreateTechnologyRequest validRequest;

    private User testUserAdminA;
    private User testUserA;

    @InjectMocks
    private TechnologyServiceImpl technologyService;

    @BeforeEach
    void setUp(){
        validRequest = new CreateTechnologyRequest();
        validRequest.setName("New Technology");

        testUserAdminA = TestDataUtil.createTestUserAdminA();
        testUserA = TestDataUtil.createTestUserA();
    }

    @Test
    public void TechnologyService_createTechnology_Success() throws TechnologyWithThisNameAlreadyExistsException{
        when(technologyRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.empty());

        TechnologyDto result = technologyService.createTechnology(validRequest, "some jwt");

        assertNotNull(result);
        assertEquals(result.getName(), validRequest.getName());
        verify(technologyRepository, times(1)).save(Mockito.any(Technology.class));

    }

    @Test
    public void TechnologyService_createTechnology_ThrowsTechnologyWithThisNameAlreadyExistsException(){
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(technologyRepository.findByName(Mockito.any(String.class))).thenReturn(Optional.of(existingTechnology));

        assertThrows(TechnologyWithThisNameAlreadyExistsException.class, () -> {
            technologyService.createTechnology(validRequest, "some jwt");}
        );
        verify(technologyRepository, never()).save(any(Technology.class));
    }

    @Test
    public void TechnologyService_deleteTechnologyById_Success() throws TechnologyNotFoundByIdException, UserNotFoundByJwtException, NoPermissionException {
        when(userService.findUserByJwt(Mockito.any(String.class))).thenReturn(testUserAdminA);


        Long validId = 1L;
        Technology technologyToDelete = new Technology();
        technologyToDelete.setId(validId);

        Offer offer1 = mock(Offer.class);
        Offer offer2 = mock(Offer.class);
        Offer offer3 = mock(Offer.class);

        technologyToDelete.setAssignedAsNiceToHave(new ArrayList<>(List.of(offer1, offer2)));
        technologyToDelete.setAssignedAsRequired(new ArrayList<>(List.of(offer1, offer2, offer3)));


        when(technologyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(technologyToDelete));


        technologyService.deleteTechnologyById(validId, "some jwt");

        verify(offerRepository, times(2)).save(eq(offer1));
        verify(offerRepository, times(2)).save(eq(offer2));
        verify(offerRepository, times(1)).save(eq(offer3));
        verify(technologyRepository, times(1)).delete(eq(technologyToDelete));
    }

    @Test
    public void TechnologyService_deleteTechnologyById_ThrowsTechnologyNotFoundByIdException() throws UserNotFoundByJwtException, NoPermissionException, TechnologyNotFoundByIdException {
        when(technologyRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        when(userService.findUserByJwt(Mockito.any(String.class))).thenReturn(testUserAdminA);

        Long invalidId = 1L;

        assertThrows(TechnologyNotFoundByIdException.class, () -> technologyService.deleteTechnologyById(invalidId, "some jwt"));
        verify(technologyRepository, never()).delete(Mockito.any(Technology.class));
        verify(offerRepository, never()).save(Mockito.any(Offer.class));
    }

    @Test
    public void TechnologyService_deleteTechnologyById_ThrowsNoPermissionException() throws UserNotFoundByJwtException, NoPermissionException, TechnologyNotFoundByIdException {
        when(userService.findUserByJwt(Mockito.any(String.class))).thenReturn(testUserAdminA);
        doThrow(new NoPermissionException()).when(utilityService).validatePermissionIsAdmin(testUserAdminA);

        Long validId = 1L;

        assertThrows(NoPermissionException.class, () -> technologyService.deleteTechnologyById(validId, "some jwt"));
        verify(technologyRepository, never()).delete(Mockito.any(Technology.class));
        verify(offerRepository, never()).save(Mockito.any(Offer.class));
    }

    @Test
    public void TechnologyService_updateTechnology_ReturnsTechnologyDto() throws UserNotFoundByJwtException, NoPermissionException, TechnologyNotFoundByIdException {
        Long validId = 1L;
        String validJwt = "some jwt";
        Technology existingTechnology = new Technology();
        existingTechnology.setName("Existing");

        when(userService.findUserByJwt(validJwt)).thenReturn(testUserAdminA);

        when(technologyRepository.findById(validId)).thenReturn(Optional.of(existingTechnology));

        TechnologyDto result = technologyService.updateTechnology(validRequest, validId, validJwt);

        assertNotNull(result);
        assertEquals(result.getName(), validRequest.getName());
        verify(technologyRepository, times(1)).save(existingTechnology);
    }

    @Test
    public void TechnologyService_updateTechnology_ThrowsTechnologyNotFoundByIdException() throws UserNotFoundByJwtException, NoPermissionException, TechnologyNotFoundByIdException {
        Long id = 1L;
        String validJwt = "some jwt";

        when(userService.findUserByJwt(validJwt)).thenReturn(testUserAdminA);

        when(technologyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotFoundByIdException.class, () -> technologyService.updateTechnology(validRequest, id, validJwt));
    }

    @Test
    public void TechnologyService_updateTechnology_ThrowsNoPermissionException() throws UserNotFoundByJwtException, NoPermissionException, TechnologyNotFoundByIdException {
        Long id = 1L;
        String validJwt = "some jwt";

        when(userService.findUserByJwt(validJwt)).thenReturn(testUserAdminA);

        doThrow(new NoPermissionException()).when(utilityService).validatePermissionIsAdmin(testUserAdminA);

        assertThrows(NoPermissionException.class, () -> technologyService.updateTechnology(validRequest, id, validJwt));
    }




//
//    @Test
//    public void TechnologyService_getTechnologyById_ReturnsTechnologyDto() throws TechnologyNotFoundByIdException {
//        Long validId = 1L;
//        String validJwt = "some jwt";
//        Technology existingTechnology = new Technology();
//        existingTechnology.setName("Existing");
//
//        when(technologyRepository.findById(validId)).thenReturn(Optional.of(existingTechnology));
//
//        TechnologyDto result = technologyService.getTechnologyById(validId, validJwt);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    public void TechnologyService_getTechnologyById_ThrowsTechnologyNotFoundByIdException(){
//        Long validId = 1L;
//        String validJwt = "some jwt";
//
//        when(technologyRepository.findById(validId)).thenReturn(Optional.empty());
//
//        assertThrows(TechnologyNotFoundByIdException.class, () -> {
//            technologyService.getTechnologyById(validId, validJwt);
//        });
//    }
//
//    @Test
//    public void TechnologyService_addAssignedAsNiceHave_Success(){
//        Technology technology = new Technology();
//        Offer offer = new Offer();
//        assertFalse(technology.getAssignedAsNiceToHave().contains(offer));
//
//        technologyService.addAssignedAsNiceToHave(technology, offer);
//
//        verify(technologyRepository, times(1)).save(technology);
//        verify(offerRepository, times(1)).save(offer);
//        assertTrue(technology.getAssignedAsNiceToHave().contains(offer));
//        assertTrue(offer.getNiceToHaveTechnologies().contains(technology));
//
//    }
//
//    @Test
//    public void TechnologyService_addAssignedAsRequired_Success(){
//        Technology technology = new Technology();
//        Offer offer = new Offer();
//        assertFalse(technology.getAssignedAsRequired().contains(offer));
//
//        technologyService.addAssignedAsRequired(technology, offer);
//
//        verify(technologyRepository, times(1)).save(technology);
//        verify(offerRepository, times(1)).save(offer);
//        assertTrue(technology.getAssignedAsRequired().contains(offer));
//        assertTrue(offer.getRequiredTechnologies().contains(technology));
//
//    }
//
//    @Test
//    public void TechnologyService_deleteOfferFromAssignedAsRequired_Success(){
//        Technology technology = new Technology();
//        Offer offer = new Offer();
//
//        technologyService.addAssignedAsRequired(technology, offer);
//
//        assertTrue(technology.getAssignedAsRequired().contains(offer));
//
//        technologyService.deleteOfferFromAssignedAsRequired(technology, offer);
//
//        assertFalse(technology.getAssignedAsRequired().contains(offer));
//        assertFalse(offer.getRequiredTechnologies().contains(technology));
//    }
//
//    @Test
//    public void TechnologyService_deleteOfferFromAssignedAsNiceToHace_Success(){
//        Technology technology = new Technology();
//        Offer offer = new Offer();
//
//        technologyService.addAssignedAsNiceToHave(technology, offer);
//
//        assertTrue(technology.getAssignedAsNiceToHave().contains(offer));
//
//        technologyService.deleteOfferFromAssignedAsNiceToHave(technology, offer);
//
//        assertFalse(technology.getAssignedAsNiceToHave().contains(offer));
//        assertFalse(offer.getNiceToHaveTechnologies().contains(technology));
//    }




}
