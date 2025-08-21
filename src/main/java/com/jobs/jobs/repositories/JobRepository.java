package com.jobs.jobs.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jobs.jobs.entities.Jobs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Jobs,Integer>, JpaSpecificationExecutor<Jobs>{

    // @Query("SELECT j FROM Jobs j " +
    //        "WHERE (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
    //        "AND ( :keyword IS NULL OR " +
    //        "      LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    //        "      LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
    //        "      LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) ) " +
    //        "AND (:salaryMin IS NULL OR j.salaryMin >= :salaryMin) " +
    //        "AND (:salaryMax IS NULL OR j.salaryMax <= :salaryMax)")
    // List<Jobs> searchJobs(@Param("keyword") String keyword,
    //                       @Param("location") String location,
    //                       @Param("salaryMin") BigDecimal salaryMin,
    //                       @Param("salaryMax") BigDecimal salaryMax);




    

    Optional<Jobs> findBySourceIdAndSourceJobId(String sourceId, String sourceJobId);

    Optional<Jobs> getDistinctByDescription(String description);

    List<Jobs> queryDistinctByLocationContains(String location);
}
