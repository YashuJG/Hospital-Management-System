package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.FeedbackRequest;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Feedback;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.FeedbackRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public Feedback createFeedback(FeedbackRequest request, Long patientId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with ID: " + patientId);
        }
        
        Feedback feedback = new Feedback();
        feedback.setPatient(patientOpt.get());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());
        feedback.setServiceRating(request.getServiceRating());
        feedback.setCleanlinessRating(request.getCleanlinessRating());
        feedback.setWaitingTimeRating(request.getWaitingTimeRating());
        feedback.setStaffFriendlinessRating(request.getStaffFriendlinessRating());
        feedback.setAnonymous(request.isAnonymous());
        
       
        try {
            feedback.setFeedbackType(Feedback.FeedbackType.valueOf(request.getFeedbackType()));
        } catch (IllegalArgumentException e) {
            feedback.setFeedbackType(Feedback.FeedbackType.GENERAL);
        }
        
      
        if (request.getDoctorId() != null) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
            if (doctorOpt.isPresent()) {
                feedback.setDoctor(doctorOpt.get());
            }
        }
        
        
        if (request.getAppointmentId() != null) {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(request.getAppointmentId());
            if (appointmentOpt.isPresent()) {
                feedback.setAppointment(appointmentOpt.get());
            }
        }
        
        return feedbackRepository.save(feedback);
    }
    
    public List<Feedback> getFeedbackByPatient(Long patientId) {
        return feedbackRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }
    
    public List<Feedback> getFeedbackByDoctor(Long doctorId) {
        return feedbackRepository.findByDoctorIdOrderByCreatedAtDesc(doctorId);
    }
    
    public List<Feedback> getFeedbackByAppointment(Long appointmentId) {
        return feedbackRepository.findByAppointmentIdOrderByCreatedAtDesc(appointmentId);
    }
    
    public List<Feedback> getApprovedFeedback() {
        return feedbackRepository.findByApprovedTrueOrderByCreatedAtDesc();
    }
    
    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }
    
    public List<Feedback> getFeedbackByType(Feedback.FeedbackType feedbackType) {
        return feedbackRepository.findByFeedbackTypeOrderByCreatedAtDesc(feedbackType);
    }
    
    public List<Feedback> getFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    public Feedback approveFeedback(Long id) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isEmpty()) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
        
        Feedback feedback = feedbackOpt.get();
        feedback.setApproved(true);
        return feedbackRepository.save(feedback);
    }
    
    public Feedback rejectFeedback(Long id, String reason) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isEmpty()) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
        
        Feedback feedback = feedbackOpt.get();
        feedback.setApproved(false);
        feedback.setAdminResponse(reason);
        return feedbackRepository.save(feedback);
    }
    
    public Feedback addAdminResponse(Long id, String response) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isEmpty()) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
        
        Feedback feedback = feedbackOpt.get();
        feedback.setAdminResponse(response);
        return feedbackRepository.save(feedback);
    }
    
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
        feedbackRepository.deleteById(id);
    }
    
    public Double getAverageRatingByDoctor(Long doctorId) {
        return feedbackRepository.getAverageRatingByDoctor(doctorId);
    }
    
    public Double getOverallAverageRating() {
        return feedbackRepository.getOverallAverageRating();
    }
    
    public Long getPositiveFeedbackCount() {
        return feedbackRepository.countPositiveFeedback();
    }
    
    public Long getNegativeFeedbackCount() {
        return feedbackRepository.countNegativeFeedback();
    }
    
    public List<Feedback> searchFeedbackByKeyword(String keyword) {
        return feedbackRepository.searchByKeyword(keyword);
    }
    
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    public boolean hasPatientGivenFeedbackForAppointment(Long patientId, Long appointmentId) {
        List<Feedback> existingFeedback = feedbackRepository.findByPatientAndAppointment(patientId, appointmentId);
        return !existingFeedback.isEmpty();
    }
} 