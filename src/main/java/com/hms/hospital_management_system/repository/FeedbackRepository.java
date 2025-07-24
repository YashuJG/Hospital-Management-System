package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    List<Feedback> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    
    List<Feedback> findByDoctorIdOrderByCreatedAtDesc(Long doctorId);
    
    List<Feedback> findByAppointmentIdOrderByCreatedAtDesc(Long appointmentId);
    
    List<Feedback> findByFeedbackTypeOrderByCreatedAtDesc(Feedback.FeedbackType feedbackType);
    
    List<Feedback> findByApprovedTrueOrderByCreatedAtDesc();
    
    List<Feedback> findByApprovedFalseOrderByCreatedAtDesc();
    
    List<Feedback> findByAnonymousTrueOrderByCreatedAtDesc();
    
    @Query("SELECT f FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate")
    List<Feedback> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.doctor.id = :doctorId")
    Double getAverageRatingByDoctor(@Param("doctorId") Long doctorId);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.approved = true")
    Double getOverallAverageRating();
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.rating >= 4 AND f.approved = true")
    Long countPositiveFeedback();
    
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.rating <= 2 AND f.approved = true")
    Long countNegativeFeedback();
    
    @Query("SELECT f FROM Feedback f WHERE f.patient.id = :patientId AND f.appointment.id = :appointmentId")
    List<Feedback> findByPatientAndAppointment(@Param("patientId") Long patientId, 
                                              @Param("appointmentId") Long appointmentId);
    
    @Query("SELECT f FROM Feedback f WHERE f.comment LIKE %:keyword% AND f.approved = true")
    List<Feedback> searchByKeyword(@Param("keyword") String keyword);
} 