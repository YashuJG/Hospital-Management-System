package com.hms.hospital_management_system.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {
    
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
    
    @Column(name = "bill_number", unique = true, nullable = false)
    private String billNumber;
    
    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee;
    
    @Column(name = "medication_cost", precision = 10, scale = 2)
    private BigDecimal medicationCost;
    
    @Column(name = "lab_test_cost", precision = 10, scale = 2)
    private BigDecimal labTestCost;
    
    @Column(name = "room_charges", precision = 10, scale = 2)
    private BigDecimal roomCharges;
    
    @Column(name = "procedure_cost", precision = 10, scale = 2)
    private BigDecimal procedureCost;
    
    @Column(name = "other_charges", precision = 10, scale = 2)
    private BigDecimal otherCharges;
    
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount;
    
    @Column(name = "balance_amount", precision = 10, scale = 2)
    private BigDecimal balanceAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, INSURANCE, ONLINE_PAYMENT
    }
    
    public enum PaymentStatus {
        PENDING, PARTIAL, PAID, PENDING_WAITING_FOR_CONFIRMATION, REJECTED, OVERDUE
    }
    
    
    public Bill() {
        this.billDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
        this.consultationFee = BigDecimal.ZERO;
        this.medicationCost = BigDecimal.ZERO;
        this.labTestCost = BigDecimal.ZERO;
        this.roomCharges = BigDecimal.ZERO;
        this.procedureCost = BigDecimal.ZERO;
        this.otherCharges = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.paidAmount = BigDecimal.ZERO;
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
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }
    
    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    
    public void calculateTotalAmount() {
        BigDecimal subtotal = consultationFee.add(medicationCost).add(labTestCost)
                .add(roomCharges).add(procedureCost).add(otherCharges);
        
        BigDecimal amountAfterDiscount = subtotal.subtract(discountAmount);
        this.totalAmount = amountAfterDiscount.add(taxAmount);
        this.balanceAmount = this.totalAmount.subtract(paidAmount);
    }
} 