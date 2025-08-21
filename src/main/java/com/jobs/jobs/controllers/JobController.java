package com.jobs.jobs.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.jobs.jobs.entities.Jobs;
import com.jobs.jobs.repositories.JobRepository;

@RestController
@RequestMapping("api/jobs")
public class JobController {

    @Autowired
    JobRepository jobRepo;


    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody Jobs jobs) {
        boolean exists = jobRepo.findBySourceIdAndSourceJobId(jobs.getSourceId(), jobs.getSourceJobId()).isPresent();
        if (exists) {
            return ResponseEntity.badRequest().body("Job with the same source_id and source_job_id already exists.");
        }

        Jobs saved = jobRepo.save(jobs);
        return ResponseEntity.created(URI.create("/api/jobs/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jobs> getJobById(@PathVariable Integer id) {
        return ResponseEntity.of(jobRepo.findById(id));
    }

    @GetMapping
    public Page<Jobs> listJobs(Pageable pageable) {
        int size = Math.min(Math.max(pageable.getPageSize(), 1), 20);
        int page = Math.max(pageable.getPageNumber(), 0);
        Pageable safe = PageRequest.of(page, size, pageable.getSort());
        return jobRepo.findAll(safe);
    }

    // IGNORE THIS FUNCTION
    // @GetMapping("/search")
    // public ResponseEntity<List<Jobs>> searchJobs(
    //         @RequestParam (required = false) String keyword,
    //         @RequestParam (required = false) String location,
    //         @RequestParam (required = false) BigDecimal salary_min,
    //         @RequestParam (required = false) BigDecimal salary_max
    //         //@RequestParam (required = false) String sentiment
    // ) {
    //     List<Jobs> jobs = jobRepo.searchJobs(keyword, location, salary_min, salary_max);
    //     return new ResponseEntity<>(jobs, HttpStatus.OK);
    // }

     @GetMapping("/search2")
    public List<Jobs> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(name = "salary_min", required = false) BigDecimal salaryMin,
            @RequestParam(name = "salary_max", required = false) BigDecimal salaryMax,
            @RequestParam(required = false) String sentiment
    ) {

        var spec = buildJobsSpec(keyword, location, salaryMin, salaryMax, sentiment);
        return jobRepo.findAll(spec);
    }

    private org.springframework.data.jpa.domain.Specification<Jobs> buildJobsSpec(
            String keyword,
            String location,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            String sentiment
    ) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // location LIKE %value%
            if (hasText(location)) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            // keyword across multiple fields (title OR company OR description)
            if (hasText(keyword)) {
                String kw = "%" + keyword.toLowerCase() + "%";
                var titleP = cb.like(cb.lower(root.get("title")), kw);
                var companyP = cb.like(cb.lower(root.get("company")), kw);
                var descP = cb.like(cb.lower(root.get("description")), kw);
                predicates.add(cb.or(titleP, companyP, descP));
            }

            if (salaryMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMin"), salaryMin));
            }
            if (salaryMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaryMax"), salaryMax));
            }

            if (hasText(sentiment)) {
                // assumes a String field named 'sentiment' exists in Jobs
                predicates.add(cb.equal(cb.lower(root.get("sentiment")), sentiment.toLowerCase()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }


}
