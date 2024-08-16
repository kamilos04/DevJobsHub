package com.kamiljach.devjobshub;

import com.kamiljach.devjobshub.model.User;

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
