package com.kamiljach.devjobshub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kamiljach.devjobshub.model.Application;
import com.kamiljach.devjobshub.model.Offer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String name;

    private String surname;

    private String email;

    private Boolean isBanned = false;

    private Boolean isFirm;

    private String photoUrl;

    private Boolean isAdmin;

    private List<OfferDto> likedOffers = new ArrayList<>();

    private List<ApplicationDto> applications = new ArrayList<>();

    private List<OfferDto> offers = new ArrayList<>();
}
