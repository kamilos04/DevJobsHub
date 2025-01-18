package com.kamiljach.devjobshub.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.exceptions.exceptions.AccountAlreadyExistsException;
import com.kamiljach.devjobshub.exceptions.exceptions.UserNotFoundByEmailException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import com.kamiljach.devjobshub.request.auth.ChangePasswordRequest;
import com.kamiljach.devjobshub.request.auth.LoginRequest;
import com.kamiljach.devjobshub.request.auth.RegisterRequest;
import com.kamiljach.devjobshub.response.login.LoginResponse;
import com.kamiljach.devjobshub.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Spy
    @InjectMocks
    private AuthServiceImpl authServiceSpy;

    private User user;
    private String validJwt;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setName("Kamil");
        user.setSurname("Jach");
        user.setIsAdmin(false);
        user.setIsFirm(false);
        validJwt = "some jwt";

    }

    @Test
    public void AuthService_login_ReturnsLoginResponse() throws UserNotFoundByEmailException {
        LoginRequest validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@gmail.com");
        validLoginRequest.setPassword("password");

        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(user.getId(), validLoginRequest.getPassword()));

        when(jwtConfig.createToken(any())).thenReturn("some jwt");

        LoginResponse result = authService.login(validLoginRequest);

        assertNotNull(result.getToken());
    }


    @Test
    public void AuthService_login_ThrowsUserNotFoundByEmailException() throws UserNotFoundByEmailException {
        LoginRequest validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@gmail.com");
        validLoginRequest.setPassword("password");

        when(userRepository.findByEmail(validLoginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByEmailException.class, () -> authService.login(validLoginRequest));

    }

    @Test
    public void AuthService_register_ReturnsLoginResponse() throws UserNotFoundByEmailException, AccountAlreadyExistsException {
        RegisterRequest validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setEmail("test@gmail.com");
        validRegisterRequest.setSurname("Jach");
        validRegisterRequest.setName("Kamil");
        validRegisterRequest.setIsFirm(false);

        when(userRepository.findByEmail(validRegisterRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRegisterRequest.getPassword())).thenReturn("encodedpassword");

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(1L, validRegisterRequest.getPassword()));

        when(jwtConfig.createToken(any())).thenReturn("some jwt");

        LoginResponse result = authService.register(validRegisterRequest);

        assertNotNull(result.getToken());
        verify(userRepository).save(any());
    }


    @Test
    public void AuthService_register_ThrowsAccountAlreadyExistsException() throws UserNotFoundByEmailException, AccountAlreadyExistsException {
        RegisterRequest validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setEmail("test@gmail.com");
        validRegisterRequest.setSurname("Jach");
        validRegisterRequest.setName("Kamil");
        validRegisterRequest.setIsFirm(false);

        when(userRepository.findByEmail(validRegisterRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(AccountAlreadyExistsException.class, () -> authService.register(validRegisterRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    public void AuthService_changePasswordByJwt_ReturnsLoginResponse() {
        ChangePasswordRequest validRequest = new ChangePasswordRequest();
        validRequest.setNewPassword("newPassword");
        when(userService.findUserByJwt(validJwt)).thenReturn(user);
        when(passwordEncoder.encode(validRequest.getNewPassword())).thenReturn("encodedpassword");

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(1L, validRequest.getNewPassword()));
        when(jwtConfig.createToken(any())).thenReturn("some jwt");

        LoginResponse result = authService.changePasswordByJwt(validRequest, validJwt);

        assertNotNull(result.getToken());
        verify(userRepository).save(any());
    }


}
