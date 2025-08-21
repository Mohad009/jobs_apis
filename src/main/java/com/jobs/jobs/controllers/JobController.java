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
    @GetMapping("/search")
    public ResponseEntity<List<Jobs>> searchJobs(
            @RequestParam (required = false) String keyword,
            @RequestParam (required = false) String location,
            @RequestParam (required = false) BigDecimal salary_min,
            @RequestParam (required = false) BigDecimal salary_max
            //@RequestParam (required = false) String sentiment
    ) {
        List<Jobs> jobs = jobRepo.searchJobs(keyword, location);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/search2")
    public Map<String, String> getRequest(@RequestParam Map<String, String> multipleParams){
        String location;
        String keyword;
        String salary_min;
        String salary_max;
        System.out.printf(multipleParams.toString());
        if (multipleParams.containsKey("location")){
            location = String.valueOf(multipleParams.get("location"));
            System.out.println("Location is " + location);
        }
        if (multipleParams.containsKey("keyword")){
            keyword = String.valueOf(multipleParams.get("keyword"));
            System.out.println("Keyword is " + keyword);
        }
        if (multipleParams.containsKey("salary_min")){
            salary_min = String.valueOf(multipleParams.get("salary_min"));
            System.out.println("Salary Min is " + salary_min);
        }
        if (multipleParams.containsKey("salary_max")){
            salary_max = String.valueOf(multipleParams.get("salary_max"));
            System.out.println("Salary Max is " + salary_max);
        }
        BigDecimal salary_min2 = BigDecimal.valueOf(Double.parseDouble(multipleParams.get("salary_min")));
        BigDecimal salary_max2 = BigDecimal.valueOf(Double.parseDouble(multipleParams.get("salary_max")));


        return multipleParams;
    }


}
