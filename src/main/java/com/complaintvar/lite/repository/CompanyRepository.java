package com.complaintvar.lite.repository;

import com.complaintvar.lite.entity.Company;
import com.complaintvar.lite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT co FROM Company co WHERE co.id=?1")
    Company findCompanyByID(Long Id);
    @Query("SELECT co FROM Company co WHERE co.email=?1")
    Company findCompanyByEmail(String email);
    @Query(value = "SELECT * FROM company as co WHERE co.id < :q_id", nativeQuery = true)
    List<Company> getIdLowerThan(@Param("q_id") Long id);
}
// transaction aciliyor, native query icin ozellikle mudahale gerekebilir.
// select, (get) methodlari native methodlarda problem yok.
// create your own repository method.


//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-persistence