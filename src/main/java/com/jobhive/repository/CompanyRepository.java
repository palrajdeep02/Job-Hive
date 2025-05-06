package com.jobhive.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobhive.entity.Company;
import com.jobhive.entity.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByUser(User user);
} 
