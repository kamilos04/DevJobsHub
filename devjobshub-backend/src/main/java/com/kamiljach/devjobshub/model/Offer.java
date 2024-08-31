package com.kamiljach.devjobshub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    private OFFER_JOB_LEVEL jobLevel;

    @Enumerated(EnumType.STRING)
    private OFFER_OPERATING_MODE operatingMode;

    private Long minSalary;

    private Long maxSalary;

    private Boolean isSalaryMonthly;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "offer_user_candidates",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> candidates = new ArrayList<>();

    private String localization;

    private LocalDateTime dateTimeOfCreation;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likedOffers")
    private List<User> likedByUsers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="offer_required_technology",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> requiredTechnologies = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="offer_nice_to_have_technology",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "technology_id"))
    private List<Technology> niceToHaveTechnologies = new ArrayList<>();

    private String aboutProject;

    private String responsibilitiesText;

    private List<String> responsibilities = new ArrayList<>();

    private List<String> requirements = new ArrayList<>();

    private List<String> niceToHave = new ArrayList<>();

    private List <String> whatWeOffer = new ArrayList<>();

    public void addRequiredTechnology(Technology technology){
        if(!requiredTechnologies.contains(technology)){
            requiredTechnologies.add(technology);
            technology.getAssignedAsRequired().add(this);
        }
    }

    public void addNiceToHaveTechnology(Technology technology){
        if(!niceToHaveTechnologies.contains(technology)){
            niceToHaveTechnologies.add(technology);
            technology.getAssignedAsNiceToHave().add(this);
        }
    }

    public void addCandidate(User user){
        if(!candidates.contains(user)){
            candidates.add(user);
            user.getAppliedOffers().add(this);
        }
    }

    public void addLikedByUser(User user){
        if(!likedByUsers.contains(user)){
            likedByUsers.add(user);
            user.getLikedOffers().add(this);
        }
    }
}
