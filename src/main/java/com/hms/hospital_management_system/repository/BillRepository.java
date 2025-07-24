package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    
    List<Bill> findByPatientIdOrderByBillDateDesc(Long patientId);
    
    List<Bill> findByDoctorIdOrderByBillDateDesc(Long doctorId);
    
    List<Bill> findByPaymentStatus(Bill.PaymentStatus paymentStatus);
    
    Optional<Bill> findByBillNumber(String billNumber);
    
    @Query("SELECT b FROM Bill b WHERE b.billDate BETWEEN :startDate AND :endDate")
    List<Bill> findByBillDateBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Bill b WHERE b.patient.id = :patientId AND b.paymentStatus = :status")
    List<Bill> findByPatientIdAndPaymentStatus(@Param("patientId") Long patientId, 
                                               @Param("status") Bill.PaymentStatus status);
    
    @Query("SELECT SUM(b.totalAmount) FROM Bill b WHERE b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(b.balanceAmount) FROM Bill b WHERE b.paymentStatus = 'PENDING' OR b.paymentStatus = 'PARTIAL'")
    BigDecimal getTotalPendingAmount();
    
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.paymentStatus = :status AND b.billDate BETWEEN :startDate AND :endDate")
    Long countByPaymentStatusAndDateRange(@Param("status") Bill.PaymentStatus status, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Bill b WHERE b.dueDate < :currentDate AND (b.paymentStatus = 'PENDING' OR b.paymentStatus = 'PARTIAL')")
    List<Bill> findOverdueBills(@Param("currentDate") LocalDateTime currentDate);
} 