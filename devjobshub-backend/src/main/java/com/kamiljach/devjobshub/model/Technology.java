package com.kamiljach.devjobshub.model;

import com.kamiljach.devjobshub.dto.TechnologyDto;
import com.kamiljach.devjobshub.mappers.TechnologyMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology technology = (Technology) o;
        return Objects.equals(id, technology.id);
    }

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

    public void removeOfferAssignedAsRequired(Offer offer){
        if(assignedAsRequired.contains(offer)){
            assignedAsRequired.remove(offer);
            offer.getRequiredTechnologies().remove(this);
        }
    }

    public void removeOfferAssignedAsNiceToHave(Offer offer){
        if(assignedAsNiceToHave.contains(offer)){
            assignedAsNiceToHave.remove(offer);
            offer.getNiceToHaveTechnologies().remove(this);
        }
    }

    public static TechnologyDto mapTechnologyToTechnologyDtoShallow(Technology technology){
        TechnologyDto technologyDto = TechnologyMapper.INSTANCE.technologyToTechnologyDto(technology);
        return technologyDto;
    }

}
