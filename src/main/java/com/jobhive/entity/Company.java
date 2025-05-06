package com.jobhive.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String location;

    @Column
    private String website;

    @Column
    private String industry;

    @Column
    private String contactEmail;

    @Column
    private String contactPhone;

    @Column
    private String logoUrl;

    @Column
    private String size;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Job> jobs = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
} 