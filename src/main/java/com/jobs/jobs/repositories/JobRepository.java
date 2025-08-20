package com.jobs.jobs.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jobs.jobs.entities.Jobs;
import org.springframework.data.jpa.repository.Query;

public interface JobRepository extends JpaRepository<Jobs,Integer>{

    @Query("SELECT j from Jobs j WHERE " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
            //"j.salaryMin = ?salary_min OR " +
            //"LOWER(j.salaryMax) LIKE LOWER(CONCAT('%', :salary_max, '%'))")
            // "LOWER(j.sentiment) LIKE LOWER(CONCAT('%', :sentiment, '%'))")
    public List<Jobs> searchJobs(String location, String keyword, BigDecimal salary_min, BigDecimal salary_max);
    
    Optional<Jobs> findBySourceIdAndSourceJobId(String sourceId, String sourceJobId);

    Optional<Jobs> getDistinctByDescription(String description);

    List<Jobs> queryDistinctByLocationContains(String location);
}
