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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "requiredTechnologies", cascade = CascadeType.ALL)
    private List<Offer> assignedAsRequired = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "niceToHaveTechnologies", cascade = CascadeType.ALL)
    private List<Offer> assignedAsNiceToHave = new ArrayList<>();

    public void addToAssignedAsRequired(Offer offer){
        if(!assignedAsRequired.contains(offer)){
            assignedAsRequired.add(offer);
            offer.getRequiredTechnologies().add(this);
        }
    }

    public void addToAssignedAsNiceToHave(Offer offer){
        if(!assignedAsNiceToHave.contains(offer)){
            assignedAsNiceToHave.add(offer);
            offer.getNiceToHaveTechnologies().add(this);
        }
    }
}
