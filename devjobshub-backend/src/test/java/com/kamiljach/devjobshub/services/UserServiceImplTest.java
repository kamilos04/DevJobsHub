package com.kamiljach.devjobshub.services;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private JwtConfig jwtConfig;
    @Spy
    @InjectMocks
    private UserServiceImpl underTest;

//    @Test
//    public void testThatFindUserByJwtReturnsSelectedProfile() throws UserNotFoundByJwtException {
//        String jwt = "validJwt";
//        Claims claims = mock(Claims.class);
//        String email = "test@gmail.com";
//        final User expectedUser = new User();
//        expectedUser.setEmail(email);
//
//        doReturn(claims).when(jwtConfig).parseJwtClaims(jwt);
//        doReturn(email).when(jwtConfig).getEmail(claims);
//        doReturn(Optional.of(expectedUser)).when(userRepository).findByEmail(email);
//
//        final User findUserByJwtResult = underTest.findUserByJwt(jwt);
//        assertEquals(expectedUser, findUserByJwtResult);
//        verify(underTest).findUserByJwt(jwt);
//    }
}
