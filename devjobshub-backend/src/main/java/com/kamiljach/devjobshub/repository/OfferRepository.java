package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT o FROM Offer o " +
            "LEFT JOIN o.requiredTechnologies rt " +
            "LEFT JOIN o.niceToHaveTechnologies nt " +
            "WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND (:jobLevels IS NULL OR o.jobLevel IN :jobLevels) " +
            "AND (:operatingModes IS NULL OR o.operatingMode IN :operatingModes) " +
            "AND (:localizations IS NULL OR o.localization IN :localizations) " +
            "AND (:technologies IS NULL OR rt.id IN :technologies OR nt.id IN :technologies)")


    Page<Offer> searchOffers(@Param("text") String text, @Param("jobLevels") List<String> jobLevels, @Param("operatingModes") List<String> operatingModes, @Param("localizations") List<String> localizations, @RequestParam("technologies") List<Long> technologies, Pageable pageable);

//    @Query("SELECT o FROM Offer o WHERE o.name LIKE CONCAT('%', :text, '%') " )
////            "AND o.jobLevel IN :jobLevels " +
////            "AND o.operatingMode IN :operatingModes " +
////            "AND o.localization IN :localizations ")
//    Page<Offer> searchOffers(@Param("text") String text, Pageable pageable);
}
