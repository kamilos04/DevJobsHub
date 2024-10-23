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
                .build();
    }


}
