package com.kamiljach.devjobshub.repository;

import com.kamiljach.devjobshub.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
