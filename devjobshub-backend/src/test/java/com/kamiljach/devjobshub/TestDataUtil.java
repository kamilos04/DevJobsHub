package com.kamiljach.devjobshub;

import com.kamiljach.devjobshub.config.JwtConfig;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Technology;
import com.kamiljach.devjobshub.model.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;

public class TestDataUtil {


    public static User createTestUserA() {
        User user = new User();
        user.setName("Piotr");
        user.setIsFirm(false);
        user.setIsAdmin(false);
        return user;
    }

    public static User createTestUserB() {
        User user = new User();
        user.setName("Franek");
        user.setIsFirm(false);
        user.setIsAdmin(false);
        return user;

    }

    public static User createTestUserC() {
        User user = new User();
        user.setName("Paweł");
        user.setIsFirm(false);
        user.setIsAdmin(false);
        return user;
    }

    public static Technology createTechnologyA(){
        Technology technology = new Technology();
        technology.setName("A");
        return technology;
    }

    public static Technology createTechnologyB(){
        Technology technology = new Technology();
        technology.setName("B");
        return technology;
    }

    public static Technology createTechnologyC(){
        Technology technology = new Technology();
        technology.setName("C");
        return technology;
    }

    public static Application createApplicationA(){
        Application application = new Application();
        return application;
    }



    public static User createTestUserAdminA() {
        User user = new User();
        user.setName("Piotr");
        user.setIsFirm(false);
        user.setIsAdmin(true);
        return user;
    }

    public static User createTestUserFirmA() {
        User user = new User();
        user.setName("Piotr");
        user.setIsFirm(true);
        user.setIsAdmin(false);
        return user;
    }


}
