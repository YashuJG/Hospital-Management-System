package com.hms.hospital_management_system.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    @NotNull(message = "Appointment is required")
    private Appointment appointment;
    
    @Column(nullable = false, length = 2000)
    private String diagnosis;
    
    @Column(nullable = false, length = 3000)
    private String medications;
    
    @Column(length = 1000)
    private String instructions;
    
    @Column(length = 1000)
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime prescribedAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    @Column(name = "prescription_type", nullable = false)
    private String prescriptionType;
    
    @Column(name = "follow_up_date")
    private LocalDate followUpDate;
    
    
    public Prescription() {}
    
    public Prescription(Appointment appointment, String diagnosis, String medications, String instructions) {
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.medications = medications;
        this.instructions = instructions;
    }
    
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getPrescribedAt() {
        return prescribedAt;
    }
    
    public void setPrescribedAt(LocalDateTime prescribedAt) {
        this.prescribedAt = prescribedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getPrescriptionType() {
        return prescriptionType;
    }
    
    public void setPrescriptionType(String prescriptionType) {
        this.prescriptionType = prescriptionType;
    }
    
    public LocalDate getFollowUpDate() {
        return followUpDate;
    }
    
    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 