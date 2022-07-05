package com.sikayetvar.complaint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT c FROM Complaint c WHERE c.id=?1")
    Optional<Complaint> findComplaintByID(Long id);
}
