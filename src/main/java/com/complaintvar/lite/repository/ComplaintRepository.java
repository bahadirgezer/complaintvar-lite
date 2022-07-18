package com.complaintvar.lite.repository;

import com.complaintvar.lite.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    @Query("SELECT c FROM Complaint c WHERE c.id=?1")
    Complaint findComplaintByID(Long id);
}
