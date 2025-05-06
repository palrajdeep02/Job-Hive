package com.jobhive.repository;

import com.jobhive.entity.Resume;
import com.jobhive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUser(User user);
    Resume findFirstByUserOrderByUploadedDateDesc(User user);
} 
