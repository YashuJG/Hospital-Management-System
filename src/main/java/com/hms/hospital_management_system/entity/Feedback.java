package com.hms.hospital_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;
    
    @NotBlank(message = "Feedback comment is required")
    @Size(min = 10, max = 1000, message = "Feedback must be between 10 and 1000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;
    
    @Column(name = "service_rating")
    @Min(value = 1, message = "Service rating must be at least 1")
    @Max(value = 5, message = "Service rating must be at most 5")
    private Integer serviceRating;
    
    @Column(name = "cleanliness_rating")
    @Min(value = 1, message = "Cleanliness rating must be at least 1")
    @Max(value = 5, message = "Cleanliness rating must be at most 5")
    private Integer cleanlinessRating;
    
    @Column(name = "waiting_time_rating")
    @Min(value = 1, message = "Waiting time rating must be at least 1")
    @Max(value = 5, message = "Waiting time rating must be at most 5")
    private Integer waitingTimeRating;
    
    @Column(name = "staff_friendliness_rating")
    @Min(value = 1, message = "Staff friendliness rating must be at least 1")
    @Max(value = 5, message = "Staff friendliness rating must be at most 5")
    private Integer staffFriendlinessRating;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", nullable = false)
    private FeedbackType feedbackType = FeedbackType.GENERAL;
    
    @Column(name = "is_anonymous", nullable = false)
    private boolean anonymous = false;
    
    @Column(name = "is_approved", nullable = false)
    private boolean approved = false;
    
    @Column(name = "admin_response")
    private String adminResponse;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum FeedbackType {
        GENERAL, APPOINTMENT, DOCTOR, SERVICE, FACILITY, EMERGENCY
    }
    
    // Constructors
    public Feedback() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Feedback(Patient patient, Integer rating, String comment) {
        this.patient = patient;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
    
   
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Integer getServiceRating() {
        return serviceRating;
    }
    
    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }
    
    public Integer getCleanlinessRating() {
        return cleanlinessRating;
    }
    
    public void setCleanlinessRating(Integer cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }
    
    public Integer getWaitingTimeRating() {
        return waitingTimeRating;
    }
    
    public void setWaitingTimeRating(Integer waitingTimeRating) {
        this.waitingTimeRating = waitingTimeRating;
    }
    
    public Integer getStaffFriendlinessRating() {
        return staffFriendlinessRating;
    }
    
    public void setStaffFriendlinessRating(Integer staffFriendlinessRating) {
        this.staffFriendlinessRating = staffFriendlinessRating;
    }
    
    public FeedbackType getFeedbackType() {
        return feedbackType;
    }
    
    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
    
    public boolean isApproved() {
        return approved;
    }
    
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
    public String getAdminResponse() {
        return adminResponse;
    }
    
    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to calculate average rating
    public double getAverageRating() {
        int totalRating = rating;
        int count = 1;
        
        if (serviceRating != null) {
            totalRating += serviceRating;
            count++;
        }
        if (cleanlinessRating != null) {
            totalRating += cleanlinessRating;
            count++;
        }
        if (waitingTimeRating != null) {
            totalRating += waitingTimeRating;
            count++;
        }
        if (staffFriendlinessRating != null) {
            totalRating += staffFriendlinessRating;
            count++;
        }
        
        return (double) totalRating / count;
    }
} 