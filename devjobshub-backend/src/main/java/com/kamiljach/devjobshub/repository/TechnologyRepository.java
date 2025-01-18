package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    public Optional<Technology> findByName(String name);

    @Query("SELECT t FROM Technology t " +
            "WHERE (LOWER(t.name) LIKE LOWER(CONCAT('%', :text, '%')))")
    Page<Technology> searchTechnologies(@Param("text") String text, Pageable pageable);
}
