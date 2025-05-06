package com.jobhive.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String experienceLevel;

    @Column(nullable = false)
    private double salary;

    @Column(nullable = false)
    private LocalDateTime postedDate;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;
} 