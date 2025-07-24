package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByActiveTrue();
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findBySpecialization(String specialization);
    boolean existsByEmail(String email);
} 