package com.kamiljach.devjobshub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kamiljach.devjobshub.request.register.RegisterRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String surname;

    @Column(length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Builder.Default
    private Boolean isBanned = false;

    private Boolean isFirm;

//    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "candidates")
//    private List<Offer> appliedOffers = new ArrayList<>();

    private String photoUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="user_offer_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Offer> likedOffers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Application> applications = new ArrayList<>();

    public void addLikedOffer(Offer offer){
        if(!likedOffers.contains(offer)){
            likedOffers.add(offer);
            offer.getLikedByUsers().add(this);
        }
    }

}
