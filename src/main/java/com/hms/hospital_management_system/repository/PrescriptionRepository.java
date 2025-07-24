package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Prescription;
import com.hms.hospital_management_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointment(Appointment appointment);
    List<Prescription> findByAppointment_Patient_Id(Long patientId);
    List<Prescription> findByAppointment_Doctor_Id(Long doctorId);
} 