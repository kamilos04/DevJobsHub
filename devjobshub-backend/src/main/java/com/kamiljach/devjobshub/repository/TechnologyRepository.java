package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    public Optional<Technology> findByName(String name);
}
