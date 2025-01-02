package com.kamiljach.devjobshub.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kamiljach.devjobshub.dto.UserDto;
import com.kamiljach.devjobshub.mappers.UserMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String surname;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Boolean isBanned = false;

    private Boolean isFirm;

    private String photoUrl;

    private Boolean isAdmin = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="user_offer_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "offer_id")
    )
    private List<Offer> likedOffers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Application> applications = new ArrayList<>();

    @ManyToMany(mappedBy = "recruiters")
    private List<Offer> offers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public void addLikedOffer(Offer offer){
        if(!likedOffers.contains(offer)){
            likedOffers.add(offer);
            offer.getLikedByUsers().add(this);
        }
    }

    public void removeLikedOffer(Offer offer){
        if(likedOffers.contains(offer)){
            likedOffers.remove(offer);
            offer.getLikedByUsers().remove(this);
        }
    }

    public static UserDto mapUserToUserDtoShallow(User user){
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);

        return userDto;
    }

    public static UserDto mapUserToUserDto(User user){
        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);

        userDto.setApplications(user.getApplications().stream().map(element -> Application.mapApplicationToApplicationDtoShallow(element)).collect(Collectors.toList()));
        userDto.setOffers(user.getOffers().stream().map(element -> Offer.mapOfferToOfferDtoShallow(element)).collect(Collectors.toList()));
        userDto.setLikedOffers(user.getLikedOffers().stream().map(element -> Offer.mapOfferToOfferDtoShallow(element)).collect(Collectors.toList()));

        return userDto;
    }




}
