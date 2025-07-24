package com.hms.hospital_management_system.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PrescriptionRequest {
    
    @NotNull(message = "Appointment is required")
    private Long appointmentId;
    
    @NotNull(message = "Prescription date is required")
    private LocalDateTime prescribedAt;
    
    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;
    
    @NotBlank(message = "Medications are required")
    private String medications;
    
    private String instructions;
    
    private String prescriptionType = "REGULAR";
    
    private java.time.LocalDate followUpDate;
    
    private String notes;
    
   
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public LocalDateTime getPrescribedAt() {
        return prescribedAt;
    }
    
    public void setPrescribedAt(LocalDateTime prescribedAt) {
        this.prescribedAt = prescribedAt;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getMedications() {
        return medications;
    }
    
    public void setMedications(String medications) {
        this.medications = medications;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getPrescriptionType() {
        return prescriptionType;
    }
    
    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }
    
    public java.time.LocalDate getFollowUpDate() {
        return followUpDate;
    }
    
    public void setFollowUpDate(java.time.LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 