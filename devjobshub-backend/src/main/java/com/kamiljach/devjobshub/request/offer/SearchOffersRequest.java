package com.kamiljach.devjobshub.request.offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchOffersRequest {
    private String text;

    private List<String> jobLevels = new ArrayList<>();

    private List<String> operatingModes = new ArrayList<>();

    private List<String> localizations = new ArrayList<>();

    private List<Long> technologies = new ArrayList<>();

    private String sortingDirection;

    private String sortBy;

    private Integer pageNumber;

    private Integer numberOfElements;




}
