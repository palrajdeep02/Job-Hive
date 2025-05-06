package com.jobhive.repository;

import com.jobhive.entity.Application;
import com.jobhive.entity.Job;
import com.jobhive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByJob(Job job);
    boolean existsByUserAndJob(User user, Job job);
} 
