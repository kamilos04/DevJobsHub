package com.kamiljach.devjobshub.dto;

import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDto {
    private Long id;

    private String name;

    private List<Offer> assignedAsRequired = new ArrayList<>();

    private List<Offer> assignedAsNiceToHave = new ArrayList<>();

    public TechnologyDto(Technology technology){
        this.setId(technology.getId());
        this.setName(technology.getName());
    }
}
