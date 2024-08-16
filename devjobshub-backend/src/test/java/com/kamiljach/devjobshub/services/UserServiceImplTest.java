package com.kamiljach.devjobshub.services;

import com.kamiljach.devjobshub.TestDataUtil;
import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private JwtConfig jwtConfig;
    @Spy
    @InjectMocks
    private UserServiceImpl underTest;

    @Test
    public void testThatGetProfileByJwtReturnsSelectedProfile() throws UserNotFoundByJwtException {
        String jwt = "validJwt";
        final User findUserByJwtResult = TestDataUtil.createTestUserA();

//        when(underTest.findUserByJwt(jwt)).thenReturn(findUserByJwtResult);
        doReturn(findUserByJwtResult).when(underTest).findUserByJwt(jwt);
        final User getProfileByJwtResult = underTest.getProfileByJwt(jwt);
        assertEquals(findUserByJwtResult, getProfileByJwtResult);
        verify(underTest).getProfileByJwt(jwt);
    }
}
