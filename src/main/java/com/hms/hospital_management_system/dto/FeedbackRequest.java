package com.hms.hospital_management_system.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class FeedbackRequest {
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @NotBlank(message = "Feedback comment is required")
    @Size(min = 10, max = 1000, message = "Feedback must be between 10 and 1000 characters")
    private String comment;
    
    @Min(value = 1, message = "Service rating must be at least 1")
    @Max(value = 5, message = "Service rating must be at most 5")
    private Integer serviceRating;
    
    @Min(value = 1, message = "Cleanliness rating must be at least 1")
    @Max(value = 5, message = "Cleanliness rating must be at most 5")
    private Integer cleanlinessRating;
    
    @Min(value = 1, message = "Waiting time rating must be at least 1")
    @Max(value = 5, message = "Waiting time rating must be at most 5")
    private Integer waitingTimeRating;
    
    @Min(value = 1, message = "Staff friendliness rating must be at least 1")
    @Max(value = 5, message = "Staff friendliness rating must be at most 5")
    private Integer staffFriendlinessRating;
    
    private String feedbackType = "GENERAL";
    
    private Long doctorId;
    
    private Long appointmentId;
    
    private boolean anonymous = false;
    
  
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
    
    public String getFeedbackType() {
        return feedbackType;
    }
    
    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }
    
    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
} 