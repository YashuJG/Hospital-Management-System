package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.Prescription;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.hms.hospital_management_system.service.BillService;
import com.hms.hospital_management_system.entity.Bill;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BillService billService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        
        if (patientOpt.isEmpty()) {
            return "redirect:/login?error=Patient not found";
        }
        
        Patient currentPatient = patientOpt.get();
        
      
        long totalAppointments = appointmentRepository.findByPatient(currentPatient).size();
        long pendingAppointments = appointmentRepository.findByPatient(currentPatient).stream()
            .filter(app -> app.getStatus() == Appointment.AppointmentStatus.PENDING)
            .count();
        long totalPrescriptions = prescriptionRepository.findByAppointment_Patient_Id(currentPatient.getId()).size();
        
       
        List<Bill> recentBills = billService.getBillsByPatient(currentPatient.getId())
            .stream()
            .filter(bill -> bill.getBillDate() != null && bill.getBillDate().isAfter(LocalDateTime.now().minusMonths(1)))
            .toList();
       
        List<Bill> pendingBills = billService.getPendingBillsByPatient(currentPatient.getId());
        
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("pendingAppointments", pendingAppointments);
        model.addAttribute("totalPrescriptions", totalPrescriptions);
        model.addAttribute("currentPatient", currentPatient);
        model.addAttribute("recentBills", recentBills);
        model.addAttribute("pendingBills", pendingBills);
        
        return "patient/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        
        if (patientOpt.isEmpty()) {
            return "redirect:/login?error=Patient not found";
        }
        
        Patient currentPatient = patientOpt.get();
        List<Appointment> appointments = appointmentRepository.findByPatient(currentPatient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("currentPatient", currentPatient);
        return "patient/appointments";
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        
        if (patientOpt.isEmpty()) {
            return "redirect:/login?error=Patient not found";
        }
        
        Patient currentPatient = patientOpt.get();
        
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null && appointment.getPatient().getId().equals(currentPatient.getId())) {
            appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        }
        return "redirect:/patient/appointments";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {
      
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        
        if (patientOpt.isEmpty()) {
            return "redirect:/login?error=Patient not found";
        }
        
        Patient currentPatient = patientOpt.get();
        List<Prescription> prescriptions = prescriptionRepository.findByAppointment_Patient_Id(currentPatient.getId());
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("currentPatient", currentPatient);
        return "patient/prescriptions";
    }
} 