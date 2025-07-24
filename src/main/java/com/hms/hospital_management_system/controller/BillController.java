package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.BillRequest;
import com.hms.hospital_management_system.entity.Bill;
import com.hms.hospital_management_system.service.BillService;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bills")
public class BillController {
    
    @Autowired
    private BillService billService;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private EmailService emailService;
    
    @GetMapping
    public String listBills(Model model) {
        List<Bill> bills = billService.getBillsByDateRange(
            LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        model.addAttribute("bills", bills);
        model.addAttribute("showAll", false);
        return "bills/list";
    }
    
    @GetMapping("/all")
    public String listAllBills(Model model) {
        List<Bill> bills = billService.getAllBills();
        model.addAttribute("bills", bills);
        model.addAttribute("showAll", true);
        return "bills/list";
    }
    
    @GetMapping("/create")
    public String showCreateBillForm(Model model) {
        model.addAttribute("billRequest", new BillRequest());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("doctors", doctorRepository.findAll());
        model.addAttribute("appointments", appointmentRepository.findAll());
        return "bills/create";
    }
    
    @PostMapping("/create")
    public String createBill(@Valid @ModelAttribute BillRequest request,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "bills/create";
        }
        
        try {
            Bill bill = billService.createBill(request);
            // Email notification to patient
            String to = bill.getPatient().getEmail();
            String name = bill.getPatient().getName();
            String billNumber = bill.getBillNumber();
            String amount = bill.getTotalAmount() != null ? bill.getTotalAmount().toString() : "0";
            emailService.sendBillCreatedNotification(to, name, billNumber, amount);
            redirectAttributes.addFlashAttribute("success", "Bill created successfully!");
            return "redirect:/bills";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create bill: " + e.getMessage());
            return "redirect:/bills/create";
        }
    }
    
    @GetMapping("/{id}")
    public String viewBill(@PathVariable Long id, Model model) {
        return billService.getBillById(id)
                .map(bill -> {
                    model.addAttribute("bill", bill);
                    return "bills/view";
                })
                .orElse("redirect:/bills");
    }
    
    @GetMapping("/{id}/edit")
    public String editBillForm(@PathVariable Long id, Model model) {
        return billService.getBillById(id)
                .map(bill -> {
                    BillRequest request = new BillRequest();
                    request.setPatientId(bill.getPatient().getId());
                    if (bill.getDoctor() != null) {
                        request.setDoctorId(bill.getDoctor().getId());
                    }
                    if (bill.getAppointment() != null) {
                        request.setAppointmentId(bill.getAppointment().getId());
                    }
                    request.setBillNumber(bill.getBillNumber());
                    request.setBillDate(bill.getBillDate());
                    request.setDueDate(bill.getDueDate());
                    request.setConsultationFee(bill.getConsultationFee());
                    request.setMedicationCost(bill.getMedicationCost());
                    request.setLabTestCost(bill.getLabTestCost());
                    request.setRoomCharges(bill.getRoomCharges());
                    request.setProcedureCost(bill.getProcedureCost());
                    request.setOtherCharges(bill.getOtherCharges());
                    request.setTaxAmount(bill.getTaxAmount());
                    request.setDiscountAmount(bill.getDiscountAmount());
                    request.setPaidAmount(bill.getPaidAmount());
                    request.setNotes(bill.getNotes());
                    
                    model.addAttribute("billRequest", request);
                    model.addAttribute("billId", id);
                    return "bills/edit";
                })
                .orElse("redirect:/bills");
    }
    
    @PostMapping("/{id}/edit")
    public String updateBill(@PathVariable Long id,
                            @Valid @ModelAttribute BillRequest request,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "bills/edit";
        }
        
        try {
            billService.updateBill(id, request);
            redirectAttributes.addFlashAttribute("success", "Bill updated successfully!");
            return "redirect:/bills/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update bill: " + e.getMessage());
            return "redirect:/bills/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteBill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            billService.deleteBill(id);
            redirectAttributes.addFlashAttribute("success", "Bill deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete bill: " + e.getMessage());
        }
        return "redirect:/bills";
    }
    
    @GetMapping("/patient/{patientId}")
    public String getPatientBills(@PathVariable Long patientId, Model model) {
        List<Bill> bills = billService.getBillsByPatient(patientId);
        model.addAttribute("bills", bills);
        model.addAttribute("patientId", patientId);
        return "bills/patient-bills";
    }
    
    @GetMapping("/doctor/{doctorId}")
    public String getDoctorBills(@PathVariable Long doctorId, Model model) {
        List<Bill> bills = billService.getBillsByDoctor(doctorId);
        model.addAttribute("bills", bills);
        model.addAttribute("doctorId", doctorId);
        return "bills/doctor-bills";
    }
    
    @GetMapping("/status/{status}")
    public String getBillsByStatus(@PathVariable Bill.PaymentStatus status, Model model) {
        List<Bill> bills = billService.getBillsByPaymentStatus(status);
        model.addAttribute("bills", bills);
        model.addAttribute("status", status);
        return "bills/status-bills";
    }
    
    @GetMapping("/overdue")
    public String getOverdueBills(Model model) {
        List<Bill> bills = billService.getOverdueBills();
        model.addAttribute("bills", bills);
        return "bills/overdue-bills";
    }
    
    @GetMapping("/{id}/payment")
    public String paymentForm(@PathVariable Long id, Model model) {
        return billService.getBillById(id)
                .map(bill -> {
                    model.addAttribute("bill", bill);
                    model.addAttribute("paymentMethods", Bill.PaymentMethod.values());
                    return "bills/payment";
                })
                .orElse("redirect:/bills");
    }
    
    @PostMapping("/{id}/payment")
    public String processPayment(@PathVariable Long id,
                                @RequestParam BigDecimal amount,
                                @RequestParam Bill.PaymentMethod paymentMethod,
                                RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== PAYMENT PROCESSING STARTED ===");
            System.out.println("Bill ID: " + id);
            System.out.println("Amount: " + amount);
            System.out.println("Payment Method: " + paymentMethod);
            
            Bill bill = billService.processPatientPayment(id, amount, paymentMethod);
            
            System.out.println("Payment processed successfully!");
            System.out.println("New Bill Status: " + bill.getPaymentStatus());
            System.out.println("New Paid Amount: " + bill.getPaidAmount());
            
            // Email notification to patient on payment submission
            String to = bill.getPatient().getEmail();
            String name = bill.getPatient().getName();
            String billNumber = bill.getBillNumber();
            String paidAmount = amount != null ? amount.toString() : "0";
            
            System.out.println("Sending email to patient: " + to);
            emailService.sendPaymentSubmittedNotification(to, name, billNumber, paidAmount);
            
            // Email notification to admin/receptionist about pending confirmation
            System.out.println("Sending email to admin for confirmation");
            emailService.sendPaymentConfirmationRequest(bill);
            
            System.out.println("=== PAYMENT PROCESSING COMPLETED ===");
            
            redirectAttributes.addFlashAttribute("success", "Payment submitted successfully! Waiting for admin confirmation.");
        } catch (Exception e) {
            System.out.println("=== PAYMENT PROCESSING ERROR ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to process payment: " + e.getMessage());
        }
        return "redirect:/bills/" + id;
    }
    
    @PostMapping("/{id}/confirm-payment")
    public String confirmPayment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Bill bill = billService.confirmPayment(id);
            // Email notification to patient on payment confirmation
            String to = bill.getPatient().getEmail();
            String name = bill.getPatient().getName();
            String billNumber = bill.getBillNumber();
            String paidAmount = bill.getPaidAmount() != null ? bill.getPaidAmount().toString() : "0";
            emailService.sendPaymentConfirmedNotification(to, name, billNumber, paidAmount);
            
            redirectAttributes.addFlashAttribute("success", "Payment confirmed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to confirm payment: " + e.getMessage());
        }
        return "redirect:/bills/" + id;
    }
    
    @PostMapping("/{id}/reject-payment")
    public String rejectPayment(@PathVariable Long id, 
                               @RequestParam(required = false) String reason,
                               RedirectAttributes redirectAttributes) {
        try {
            Bill bill = billService.rejectPayment(id, reason);
            // Email notification to patient on payment rejection
            String to = bill.getPatient().getEmail();
            String name = bill.getPatient().getName();
            String billNumber = bill.getBillNumber();
            emailService.sendPaymentRejectedNotification(to, name, billNumber, reason);
            
            redirectAttributes.addFlashAttribute("success", "Payment rejected successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reject payment: " + e.getMessage());
        }
        return "redirect:/bills/" + id;
    }
    
    @GetMapping("/reports/revenue")
    public String revenueReport(@RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate,
                               Model model) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.now();
        
        BigDecimal revenue = billService.getTotalRevenue(start, end);
        BigDecimal pendingAmount = billService.getTotalPendingAmount();
        
        model.addAttribute("revenue", revenue);
        model.addAttribute("pendingAmount", pendingAmount);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        
        return "bills/revenue-report";
    }
    
    @GetMapping("/search")
    public String searchBills(@RequestParam(required = false) String billNumber,
                             @RequestParam(required = false) Long patientId,
                             Model model) {
        if (billNumber != null && !billNumber.trim().isEmpty()) {
            return billService.getBillByNumber(billNumber)
                    .map(bill -> {
                        model.addAttribute("bills", List.of(bill));
                        return "bills/search-results";
                    })
                    .orElse("redirect:/bills");
        } else if (patientId != null) {
            List<Bill> bills = billService.getBillsByPatient(patientId);
            model.addAttribute("bills", bills);
            return "bills/search-results";
        }
        return "redirect:/bills";
    }
    
    @GetMapping("/{id}/print")
    public String printBill(@PathVariable Long id, Model model) {
        return billService.getBillById(id)
                .map(bill -> {
                    model.addAttribute("bill", bill);
                    return "bills/print";
                })
                .orElse("redirect:/bills");
    }
} 