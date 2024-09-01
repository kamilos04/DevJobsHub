package com.kamiljach.devjobshub.request.offer;

import com.kamiljach.devjobshub.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOfferRequest {
    private String name;

    private OFFER_JOB_LEVEL jobLevel;

    private OFFER_OPERATING_MODE operatingMode;

    private Long minSalary;

    private Long maxSalary;

    private Boolean isSalaryMonthly;

//    private ArrayList<Long> candidates = new ArrayList<>();

    private String localization;

//    private LocalDateTime dateTimeOfCreation;

//    private ArrayList<Long> likedByUsers = new ArrayList<>();

    private ArrayList<Long> requiredTechnologies = new ArrayList<>();

    private ArrayList<Long> niceToHaveTechnologies = new ArrayList<>();

    private String aboutProject;

    private String responsibilitiesText;

    private List<String> responsibilities = new ArrayList<>();

    private List<String> requirements = new ArrayList<>();

    private List<String> niceToHave = new ArrayList<>();

    private List <String> whatWeOffer = new ArrayList<>();
}
