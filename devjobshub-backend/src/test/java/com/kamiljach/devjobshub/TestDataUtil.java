package com.kamiljach.devjobshub;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.model.User;
import org.springframework.stereotype.Component;

public class TestDataUtil {


    public static User createTestUserA() {

        return User.builder()
                .name("Piotr")
                .surname("Kowalski")
                .email("test@gmail.com")
                .isFirm(false)
                .isAdmin(false)
                .build();
    }

    public static User createTestUserAdminA() {

        return User.builder()
                .name("Piotr")
                .surname("Kowalski")
                .email("test@gmail.com")
                .isFirm(false)
                .isAdmin(true)
                .build();
    }

    public static User createTestUserFirmA() {

        return User.builder()
                .name("Piotr")
                .surname("Kowalski")
                .email("test@gmail.com")
                .isFirm(true)
                .isAdmin(false)
                .build();
    }


}
