package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    List<MedicalRecord> findByPatientIdOrderByVisitDateDesc(Long patientId);
    
    List<MedicalRecord> findByDoctorIdOrderByVisitDateDesc(Long doctorId);
    
    List<MedicalRecord> findByPatientIdAndDoctorIdOrderByVisitDateDesc(Long patientId, Long doctorId);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.visitDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findByVisitDateBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId AND mr.diagnosis LIKE %:diagnosis%")
    List<MedicalRecord> findByPatientIdAndDiagnosisContaining(@Param("patientId") Long patientId, 
                                                              @Param("diagnosis") String diagnosis);
    
    @Query("SELECT COUNT(mr) FROM MedicalRecord mr WHERE mr.doctor.id = :doctorId AND mr.visitDate BETWEEN :startDate AND :endDate")
    Long countByDoctorAndDateRange(@Param("doctorId") Long doctorId, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT mr FROM MedicalRecord mr WHERE (:patientId IS NULL OR mr.patient.id = :patientId) AND (:diagnosis IS NULL OR mr.diagnosis LIKE %:diagnosis%) AND mr.visitDate BETWEEN :fromDate AND :toDate")
    List<MedicalRecord> findByPatientIdAndDiagnosisAndDateRange(@Param("patientId") Long patientId, @Param("diagnosis") String diagnosis, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId AND mr.visitDate BETWEEN :fromDate AND :toDate")
    List<MedicalRecord> findByPatientIdAndDateRange(@Param("patientId") Long patientId, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.diagnosis LIKE %:diagnosis% AND mr.visitDate BETWEEN :fromDate AND :toDate")
    List<MedicalRecord> findByDiagnosisAndDateRange(@Param("diagnosis") String diagnosis, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
} 