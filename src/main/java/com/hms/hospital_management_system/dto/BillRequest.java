package com.hms.hospital_management_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private Long doctorId;
    
    private Long appointmentId;
    
    @NotNull(message = "Bill number is required")
    private String billNumber;
    
    @NotNull(message = "Bill date is required")
    private LocalDateTime billDate;
    
    private LocalDateTime dueDate;
    
    @PositiveOrZero(message = "Consultation fee must be positive or zero")
    private BigDecimal consultationFee = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Medication cost must be positive or zero")
    private BigDecimal medicationCost = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Lab test cost must be positive or zero")
    private BigDecimal labTestCost = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Room charges must be positive or zero")
    private BigDecimal roomCharges = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Procedure cost must be positive or zero")
    private BigDecimal procedureCost = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Other charges must be positive or zero")
    private BigDecimal otherCharges = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Tax amount must be positive or zero")
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Discount amount must be positive or zero")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @PositiveOrZero(message = "Paid amount must be positive or zero")
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    private String notes;
    
   
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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
    
    public String getBillNumber() {
        return billNumber;
    }
    
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    public LocalDateTime getBillDate() {
        return billDate;
    }
    
    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public BigDecimal getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public BigDecimal getMedicationCost() {
        return medicationCost;
    }
    
    public void setMedicationCost(BigDecimal medicationCost) {
        this.medicationCost = medicationCost;
    }
    
    public BigDecimal getLabTestCost() {
        return labTestCost;
    }
    
    public void setLabTestCost(BigDecimal labTestCost) {
        this.labTestCost = labTestCost;
    }
    
    public BigDecimal getRoomCharges() {
        return roomCharges;
    }
    
    public void setRoomCharges(BigDecimal roomCharges) {
        this.roomCharges = roomCharges;
    }
    
    public BigDecimal getProcedureCost() {
        return procedureCost;
    }
    
    public void setProcedureCost(BigDecimal procedureCost) {
        this.procedureCost = procedureCost;
    }
    
    public BigDecimal getOtherCharges() {
        return otherCharges;
    }
    
    public void setOtherCharges(BigDecimal otherCharges) {
        this.otherCharges = otherCharges;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
} 