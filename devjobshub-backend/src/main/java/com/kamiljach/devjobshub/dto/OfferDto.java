package com.kamiljach.devjobshub.dto;

import com.kamiljach.devjobshub.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDto {
    private Long id;

    private String name;

    private OFFER_JOB_LEVEL jobLevel;

    private OFFER_OPERATING_MODE operatingMode;

    private Long minSalary;

    private Long maxSalary;

    private Boolean isSalaryMonthly;

    private List<User> candidates = new ArrayList<>();

    private String localization;

    private LocalDateTime dateTimeOfCreation;

    private List<User> likedByUsers = new ArrayList<>();

    private List<Technology> requiredTechnologies = new ArrayList<>();

    private List<Technology> niceToHaveTechnologies = new ArrayList<>();

    private String aboutProject;

    private String responsibilitiesText;

    private List<String> responsibilities = new ArrayList<>();

    private List<String> requirements = new ArrayList<>();

    private List<String> niceToHave = new ArrayList<>();

    private List <String> whatWeOffer = new ArrayList<>();

    public OfferDto(Offer offer){
        this.id = offer.getId();
        this.name = offer.getName();
        this.jobLevel = offer.getJobLevel();
        this.operatingMode = offer.getOperatingMode();
        this.minSalary = offer.getMinSalary();
        this.maxSalary = offer.getMaxSalary();
        this.isSalaryMonthly = offer.getIsSalaryMonthly();
        this.localization = offer.getLocalization();
        this.dateTimeOfCreation = offer.getDateTimeOfCreation();
        this.aboutProject = offer.getAboutProject();
        this.responsibilitiesText = offer.getResponsibilitiesText();
        offer.getResponsibilities().stream().forEach(element -> this.responsibilities.add(element));
        offer.getRequirements().stream().forEach(element -> this.requirements.add(element));
        offer.getNiceToHave().stream().forEach(element -> this.niceToHave.add(element));
        offer.getWhatWeOffer().stream().forEach(element -> this.whatWeOffer.add(element));
//        To do candidates, likedByUsers, requiredTechnolgies, niceToHaveTechnologies
    }
}