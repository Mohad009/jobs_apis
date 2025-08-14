package com.jobs.jobs.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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
public ResponseEntity<?> createJob( @RequestBody Jobs jobs){
    boolean exists=jobRepo.findBySourceIdAndSourceJobId(jobs.getSourceId(),jobs.getSourceJobId()).isPresent();
    if(exists) {return ResponseEntity.badRequest().body("Job with the same source_id and source_job_id already exists.");}
    
    Jobs saved=jobRepo.save(jobs);
    return ResponseEntity.created(URI.create("/api/jobs/"+saved.getId())).body(saved);
}

@GetMapping("/{id}")
    public ResponseEntity<Jobs> getJobById(@PathVariable Integer id){
    return ResponseEntity.of(jobRepo.findById(id));
}

}
