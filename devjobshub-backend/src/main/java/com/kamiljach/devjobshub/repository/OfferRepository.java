package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("SELECT DISTINCT o FROM Offer o " +
            "LEFT JOIN o.requiredTechnologies rt " +
            "LEFT JOIN o.niceToHaveTechnologies nt " +
            "LEFT JOIN o.responsibilities res " +
            "LEFT JOIN o.requirements req " +
            "LEFT JOIN o.niceToHave nth " +
            "LEFT JOIN o.whatWeOffer wwo " +
            "WHERE ((LOWER(o.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(o.aboutProject) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(o.responsibilitiesText) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(res) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(req) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(nth) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(wwo) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:jobLevels IS NULL OR o.jobLevel IN :jobLevels) " +
            "AND (:operatingModes IS NULL OR o.operatingMode IN :operatingModes) " +
            "AND (:specializations IS NULL OR o.specialization IN :specializations) " +
            "AND (:localizations IS NULL OR o.localization IN :localizations) " +
            "AND (:technologies IS NULL OR rt.id IN :technologies OR nt.id IN :technologies) " +
            "AND (o.expirationDate > :currentDateTime))")
    Page<Offer> searchOffers(@Param("text") String text, @Param("jobLevels") List<String> jobLevels, @Param("operatingModes") List<String> operatingModes, @Param("specializations") List<String> specializations, @Param("localizations") List<String> localizations, @Param("technologies") List<Long> technologies, @Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Offer o " +
            "LEFT JOIN o.likedByUsers lbu " +
            "WHERE (lbu.id = :userId AND o.expirationDate > :currentDateTime)")
    Page<Offer> getLikedOffersFromUser(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Offer o " +
            "LEFT JOIN o.recruiters r " +
            "WHERE ((r.id = :recruiterId) " +
            "AND (:isActive IS NULL OR (" +
            "(:isActive = TRUE AND o.expirationDate > :currentDateTime)" +
            " OR (:isActive = FALSE AND o.expirationDate <= :currentDateTime))))")
    Page<Offer> getOffersFromRecruiter(@Param("recruiterId") Long recruiterId, @Param("isActive") Boolean isActive, @Param("currentDateTime") LocalDateTime currentDateTime, Pageable pageable);
}
