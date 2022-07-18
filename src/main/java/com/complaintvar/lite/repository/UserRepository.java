package com.complaintvar.lite.repository;

import com.complaintvar.lite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id=?1")
    User findUserByID(Long Id);
    @Query("SELECT u FROM User u WHERE u.email=?1")
    User findUserByEmail(String email);
}
