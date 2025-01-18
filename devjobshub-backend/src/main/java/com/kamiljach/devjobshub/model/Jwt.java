package com.kamiljach.devjobshub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String jwt;

    private Boolean isBlocked = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public void putUser(User user){
        this.user = user;
        if(!user.getJwtList().contains(jwt)){
            user.getJwtList().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jwt jwt1 = (Jwt) o;
        return Objects.equals(id, jwt1.id) && Objects.equals(jwt, jwt1.jwt);
    }
}
