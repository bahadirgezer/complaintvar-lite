package com.complaintvar.lite.repository;

import com.complaintvar.lite.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT co FROM Company co WHERE co.id=?1")
    public Optional<Company> findCompanyByID(Long Id);
    @Query("SELECT co FROM Company co WHERE co.email=?1")
    public Optional<Company> findCompanyByEmail(String email);
}
