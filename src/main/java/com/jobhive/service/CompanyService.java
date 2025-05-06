package com.jobhive.service;

import com.jobhive.entity.Company;
import com.jobhive.entity.User;
import com.jobhive.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyByUser(User user) {
        return companyRepository.findByUser(user);
    }

    public Company updateCompany(Company company) {
        return companyRepository.save(company);
    }
} 
