package com.s_var.lite.company;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyByID(Long id) {
        Optional<Company> companyByID = companyRepository.findCompanyByID(id);

        if (!companyByID.isPresent()) {
            throw new IllegalArgumentException();
        }
        return companyByID.get();
    }

    public Company createCompany(Company companyRequest) {
        //TODO: add exception logic
        return companyRepository.save(companyRequest);
    }

    public Company updateCompany(Long id, Company companyRequest) {
        Company company = companyRepository.findCompanyByID(id)

                .orElseThrow(() -> new IllegalArgumentException()); //context specific exception

        company.setEmail(companyRequest.getEmail()); //TODO: add logic
        company.setName(companyRequest.getName());
        //TODO: update password
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        Company company = companyRepository.findCompanyByID(id)
                .orElseThrow(() -> new IllegalArgumentException());
        companyRepository.delete(company);
    }
}
