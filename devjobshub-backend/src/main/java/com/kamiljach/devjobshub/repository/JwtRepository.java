package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByJwt(String jwt);

}
