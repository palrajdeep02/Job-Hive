package com.jobhive.controller;

import com.jobhive.entity.Company;
import com.jobhive.entity.User;
import com.jobhive.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company, @AuthenticationPrincipal User user) {
        company.setUser(user);
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    @GetMapping("/my")
    public ResponseEntity<Company> getMyCompany(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(companyService.getCompanyByUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company, @AuthenticationPrincipal User user) {
        Company existingCompany = companyService.getCompanyByUser(user);
        if (!existingCompany.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        company.setId(id);
        company.setUser(user);
        return ResponseEntity.ok(companyService.updateCompany(company));
    }
} 
