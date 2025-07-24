package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.PrescriptionRequest;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Prescription;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        var doctorOpt = doctorRepository.findByEmail(userEmail);
        if (doctorOpt.isPresent()) {
            var doctor = doctorOpt.get();
            long totalAppointments = appointmentRepository.countByDoctor(doctor);
            long pendingAppointments = appointmentRepository.countByDoctorAndStatus(doctor, Appointment.AppointmentStatus.PENDING);
            long completedAppointments = appointmentRepository.countByDoctorAndStatus(doctor, Appointment.AppointmentStatus.COMPLETED);
            model.addAttribute("totalAppointments", totalAppointments);
            model.addAttribute("pendingAppointments", pendingAppointments);
            model.addAttribute("completedAppointments", completedAppointments);
        } else {
            model.addAttribute("totalAppointments", 0);
            model.addAttribute("pendingAppointments", 0);
            model.addAttribute("completedAppointments", 0);
        }
        return "doctor/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        var doctorOpt = doctorRepository.findByEmail(userEmail);
        if (doctorOpt.isPresent()) {
            var doctor = doctorOpt.get();
            List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
            model.addAttribute("appointments", appointments);
        } else {
            model.addAttribute("appointments", List.of());
        }
        return "doctor/appointments";
    }

    @PostMapping("/appointments/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            appointment.setStatus(Appointment.AppointmentStatus.valueOf(status));
            appointmentRepository.save(appointment);
        }
        return "redirect:/doctor/appointments";
    }

    @GetMapping("/prescriptions")
    public String prescriptions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        var doctorOpt = doctorRepository.findByEmail(userEmail);
        if (doctorOpt.isPresent()) {
            var doctor = doctorOpt.get();
            List<Prescription> prescriptions = prescriptionRepository.findByAppointment_Doctor_Id(doctor.getId());
            model.addAttribute("prescriptions", prescriptions);
        } else {
            model.addAttribute("prescriptions", List.of());
        }
        return "doctor/prescriptions";
    }

    @GetMapping("/create-prescription")
    public String createPrescriptionForm(Model model) {
        List<Appointment> appointments = appointmentRepository.findByStatus(Appointment.AppointmentStatus.CONFIRMED);
        model.addAttribute("appointments", appointments);
        model.addAttribute("prescriptionRequest", new PrescriptionRequest());
        return "doctor/create-prescription";
    }

    @PostMapping("/prescriptions/create")
    public String createPrescription(@Valid @ModelAttribute PrescriptionRequest prescriptionRequest,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        
        if (bindingResult.hasErrors()) {
            List<Appointment> appointments = appointmentRepository.findByStatus(Appointment.AppointmentStatus.CONFIRMED);
            model.addAttribute("appointments", appointments);
            return "doctor/create-prescription";
        }
        
        try {
            Appointment appointment = appointmentRepository.findById(prescriptionRequest.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            Prescription prescription = new Prescription();
            prescription.setAppointment(appointment);
            prescription.setDiagnosis(prescriptionRequest.getDiagnosis());
            prescription.setMedications(prescriptionRequest.getMedications());
            prescription.setInstructions(prescriptionRequest.getInstructions());
            prescription.setPrescribedAt(prescriptionRequest.getPrescribedAt());
            prescription.setPrescriptionType(prescriptionRequest.getPrescriptionType());
            prescription.setFollowUpDate(prescriptionRequest.getFollowUpDate());
            prescription.setNotes(prescriptionRequest.getNotes());
            
            prescriptionRepository.save(prescription);
            
            redirectAttributes.addFlashAttribute("success", "Prescription created successfully!");
            return "redirect:/doctor/prescriptions";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create prescription: " + e.getMessage());
            return "redirect:/doctor/create-prescription";
        }
    }

    @GetMapping("/prescriptions/print/{id}")
    public String printPrescription(@PathVariable Long id, Model model) {
        Prescription prescription = prescriptionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Prescription not found"));
        model.addAttribute("prescription", prescription);
        return "doctor/print-prescription";
    }
} 