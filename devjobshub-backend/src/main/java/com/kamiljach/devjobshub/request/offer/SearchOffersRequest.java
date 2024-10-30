package com.kamiljach.devjobshub.request.offer;

import java.util.ArrayList;

public class SearchOffersRequest {
    private String text;

    private ArrayList<String> jobLevel = new ArrayList<>();

    private ArrayList<String> operatringMode = new ArrayList<>();

    private ArrayList<String> localizations = new ArrayList<>();

    private ArrayList<Long> technologies = new ArrayList<>();

    private String sortingDirection;

    private String sortBy;

    private Long pageNumber;

    private Long numberOfElements;




}
