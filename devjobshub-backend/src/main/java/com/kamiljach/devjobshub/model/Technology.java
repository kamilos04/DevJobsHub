package com.kamiljach.devjobshub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "requiredTechnologies")
    private List<Offer> assignedAsRequired = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "niceToHaveTechnologies")
    private List<Offer> assignedAsNiceToHave = new ArrayList<>();
}
