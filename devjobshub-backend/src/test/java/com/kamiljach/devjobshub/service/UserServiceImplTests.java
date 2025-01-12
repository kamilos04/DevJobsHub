package com.kamiljach.devjobshub.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.OfferRepository;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {


    @Mock
    private UserRepository userRepository;
    @Mock
    private OfferRepository offerRepository;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private Claims claims;

    @InjectMocks
    UserServiceImpl userService;

    private String validJwt = "Valid jwt";
    private String email = "test@gmail.com";

//    @Test
//    public void UserService_findUserByJwt_ReturnsUser() throws UserNotFoundByJwtException {
//        User user = new User();
//        user.setEmail(email);
//
//        when(jwtConfig.parseJwtClaims(Mockito.any(String.class))).thenReturn(claims);
//        when(jwtConfig.getId(claims)).thenReturn(email);
//        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
//
//        User result = userService.findUserByJwt(validJwt);
//
//        assertNotNull(result);
//        assertEquals(email, result.getEmail());
//
//    }
//
//    @Test
//    public void UserService_addLikedOffer_Verify() {
//        User user = new User();
//        Offer offer = new Offer();
//
//        userService.addLikedOffer(user, offer);
//
//        verify(userRepository, times(1)).save(user);
//        verify(offerRepository, times(1)).save(offer);
//    }





}
