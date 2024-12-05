package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.BlackListJwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlackListJwtRepository extends JpaRepository<BlackListJwt, Long> {
    Optional<BlackListJwt> findByJwt(String jwt);
}
