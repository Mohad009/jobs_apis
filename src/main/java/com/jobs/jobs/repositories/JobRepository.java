package com.jobs.jobs.repositories;

import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jobs.jobs.entities.Jobs;

public interface JobRepository extends JpaRepository<Jobs,Integer>{
    Page<Jobs> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String q1, String q2, Pageable p);
    Page<Jobs> findByCompanyContainingIgnoreCase(String company, Pageable p);
    Page<Jobs> findByLocationContainingIgnoreCase(String location, Pageable p);
    Optional<Jobs> findBySourceIdAndSourceJobId(String sourceId, String sourceJobId);
}
