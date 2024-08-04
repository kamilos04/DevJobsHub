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
}
