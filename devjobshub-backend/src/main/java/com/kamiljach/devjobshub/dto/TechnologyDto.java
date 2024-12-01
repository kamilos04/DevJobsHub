package com.kamiljach.devjobshub.dto;

import com.kamiljach.devjobshub.mappers.OfferMapper;
import com.kamiljach.devjobshub.mappers.TechnologyMapper;
import com.kamiljach.devjobshub.model.Offer;
import com.kamiljach.devjobshub.model.Technology;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDto {
    private Long id;

    private String name;

    private List<OfferDto> assignedAsRequired = new ArrayList<>();

    private List<OfferDto> assignedAsNiceToHave = new ArrayList<>();

}
