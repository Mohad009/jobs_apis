package com.jobs.jobs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobs.jobs.entities.Jobs;

public interface JobRepository extends JpaRepository<Jobs,Integer>{
}
