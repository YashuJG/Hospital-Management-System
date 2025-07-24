package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.BillRequest;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Bill;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.BillRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    
    @Autowired
    private BillRepository billRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public Bill createBill(BillRequest request) {
        Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
        
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with ID: " + request.getPatientId());
        }
        
        Bill bill = new Bill();
        bill.setPatient(patientOpt.get());
        bill.setBillNumber(request.getBillNumber());
        bill.setBillDate(request.getBillDate());
        bill.setDueDate(request.getDueDate());
        bill.setConsultationFee(request.getConsultationFee());
        bill.setMedicationCost(request.getMedicationCost());
        bill.setLabTestCost(request.getLabTestCost());
        bill.setRoomCharges(request.getRoomCharges());
        bill.setProcedureCost(request.getProcedureCost());
        bill.setOtherCharges(request.getOtherCharges());
        bill.setTaxAmount(request.getTaxAmount());
        bill.setDiscountAmount(request.getDiscountAmount());
        bill.setPaidAmount(request.getPaidAmount());
        bill.setNotes(request.getNotes());
        
     
        if (request.getDoctorId() != null) {
            Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
            if (doctorOpt.isPresent()) {
                bill.setDoctor(doctorOpt.get());
            }
        }
        
      
        if (request.getAppointmentId() != null) {
            Optional<Appointment> appointmentOpt = appointmentRepository.findById(request.getAppointmentId());
            if (appointmentOpt.isPresent()) {
                bill.setAppointment(appointmentOpt.get());
            }
        }
        
        
        bill.calculateTotalAmount();
        
   
        if (bill.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        } else if (bill.getPaidAmount().compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        
        return billRepository.save(bill);
    }
    
    public List<Bill> getBillsByPatient(Long patientId) {
        return billRepository.findByPatientIdOrderByBillDateDesc(patientId);
    }
    
    public List<Bill> getBillsByDoctor(Long doctorId) {
        return billRepository.findByDoctorIdOrderByBillDateDesc(doctorId);
    }
    
    public List<Bill> getBillsByPaymentStatus(Bill.PaymentStatus status) {
        return billRepository.findByPaymentStatus(status);
    }
    
    public List<Bill> getBillsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return billRepository.findByBillDateBetween(startDate, endDate);
    }
    
    public List<Bill> getPendingBillsByPatient(Long patientId) {
        return billRepository.findByPatientIdAndPaymentStatus(patientId, Bill.PaymentStatus.PENDING);
    }
    
    public List<Bill> getOverdueBills() {
        return billRepository.findOverdueBills(LocalDateTime.now());
    }
    
    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }
    
    public Optional<Bill> getBillByNumber(String billNumber) {
        return billRepository.findByBillNumber(billNumber);
    }
    
    public Bill updateBill(Long id, BillRequest request) {
        Optional<Bill> existingBill = billRepository.findById(id);
        
        if (existingBill.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + id);
        }
        
        Bill bill = existingBill.get();
        bill.setBillNumber(request.getBillNumber());
        bill.setBillDate(request.getBillDate());
        bill.setDueDate(request.getDueDate());
        bill.setConsultationFee(request.getConsultationFee());
        bill.setMedicationCost(request.getMedicationCost());
        bill.setLabTestCost(request.getLabTestCost());
        bill.setRoomCharges(request.getRoomCharges());
        bill.setProcedureCost(request.getProcedureCost());
        bill.setOtherCharges(request.getOtherCharges());
        bill.setTaxAmount(request.getTaxAmount());
        bill.setDiscountAmount(request.getDiscountAmount());
        bill.setPaidAmount(request.getPaidAmount());
        bill.setNotes(request.getNotes());
        
     
        bill.calculateTotalAmount();
        
       
        if (bill.getPaidAmount().compareTo(BigDecimal.ZERO) == 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        } else if (bill.getPaidAmount().compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        
        return billRepository.save(bill);
    }
    
    public Bill processPayment(Long billId, BigDecimal amount, Bill.PaymentMethod paymentMethod) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        
        if (billOpt.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }
        
        Bill bill = billOpt.get();
        BigDecimal newPaidAmount = bill.getPaidAmount().add(amount);
        bill.setPaidAmount(newPaidAmount);
        bill.setPaymentMethod(paymentMethod);
        bill.setPaymentDate(LocalDateTime.now());
        
        // Update payment status
        if (newPaidAmount.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        
        bill.calculateTotalAmount();
        
        return billRepository.save(bill);
    }
    
    public Bill processPatientPayment(Long billId, BigDecimal amount, Bill.PaymentMethod paymentMethod) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        
        if (billOpt.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }
        
        Bill bill = billOpt.get();
        BigDecimal newPaidAmount = bill.getPaidAmount().add(amount);
        bill.setPaidAmount(newPaidAmount);
        bill.setPaymentMethod(paymentMethod);
        bill.setPaymentDate(LocalDateTime.now());
        
        // Set status to waiting for confirmation
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING_WAITING_FOR_CONFIRMATION);
        
        bill.calculateTotalAmount();
        
        return billRepository.save(bill);
    }
    
    public Bill confirmPayment(Long billId) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        
        if (billOpt.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }
        
        Bill bill = billOpt.get();
        
        if (bill.getPaymentStatus() != Bill.PaymentStatus.PENDING_WAITING_FOR_CONFIRMATION) {
            throw new RuntimeException("Bill is not in pending confirmation status");
        }
        
      
        if (bill.getPaidAmount().compareTo(bill.getTotalAmount()) >= 0) {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
        } else {
            bill.setPaymentStatus(Bill.PaymentStatus.PARTIAL);
        }
        
        bill.calculateTotalAmount();
        
        return billRepository.save(bill);
    }
    
    public Bill rejectPayment(Long billId, String reason) {
        Optional<Bill> billOpt = billRepository.findById(billId);
        
        if (billOpt.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId);
        }
        
        Bill bill = billOpt.get();
        
        if (bill.getPaymentStatus() != Bill.PaymentStatus.PENDING_WAITING_FOR_CONFIRMATION) {
            throw new RuntimeException("Bill is not in pending confirmation status");
        }
        
        
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setPaymentMethod(null);
        bill.setPaymentDate(null);
        
   
        String currentNotes = bill.getNotes() != null ? bill.getNotes() : "";
        String rejectionNote = "\n[PAYMENT REJECTED: " + (reason != null ? reason : "No reason provided") + " - " + LocalDateTime.now() + "]";
        bill.setNotes(currentNotes + rejectionNote);
        
        bill.calculateTotalAmount();
        
        return billRepository.save(bill);
    }
    
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found with ID: " + id);
        }
        billRepository.deleteById(id);
    }
    
    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = billRepository.getTotalRevenueBetweenDates(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalPendingAmount() {
        BigDecimal pendingAmount = billRepository.getTotalPendingAmount();
        return pendingAmount != null ? pendingAmount : BigDecimal.ZERO;
    }
    
    public Long getBillCountByStatus(Bill.PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return billRepository.countByPaymentStatusAndDateRange(status, startDate, endDate);
    }
    
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
} 