package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.APPLICATION_STATUS;
import com.kamiljach.devjobshub.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT DISTINCT a FROM Application a " +
            "LEFT JOIN a.offer o " +
            "WHERE (o.id = :offerId )")
    Page<Application> getApplicationsFromOffer(@Param("offerId") Long offerId, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Application a " +
            "LEFT JOIN a.offer o " +
            "WHERE ((o.id = :offerId ) " +
            "AND (a.status = :status))")
    Page<Application> getApplicationsFromOfferWithParticularStatus(@Param("offerId") Long offerId, @Param("status") APPLICATION_STATUS status, Pageable pageable);
}
