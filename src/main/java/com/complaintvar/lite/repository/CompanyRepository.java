package com.complaintvar.lite.repository;

import com.complaintvar.lite.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT co FROM Company co WHERE co.id=?1")
    Company findCompanyByID(Long Id);
    @Query("SELECT co FROM Company co WHERE co.email=?1")
    Company findCompanyByEmail(String email);
    @Query(value = "UPDATE sikayetvar_lite.company SET sikayetvar_lite.company.verified=?1 WHERE *", nativeQuery = true)
    void updateEveryVerification(Boolean verification);
}
