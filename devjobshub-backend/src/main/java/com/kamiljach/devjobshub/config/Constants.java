package com.kamiljach.devjobshub.config;

import org.springframework.beans.factory.annotation.Value;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static long expirationTime = 3600000;

    public static String header = "Authorization";

    public static String prefix = "Bearer ";

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
}
