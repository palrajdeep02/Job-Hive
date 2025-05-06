package com.jobhive.repository;

import com.jobhive.entity.Company;
import com.jobhive.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompany(Company company);
} 