package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.User;
import com.hms.hospital_management_system.service.AppointmentService;
import com.hms.hospital_management_system.service.PasswordResetService;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        List<Doctor> allDoctors = doctorRepository.findAll();
        List<Patient> allPatients = patientRepository.findAll();
        
        model.addAttribute("totalAppointments", allAppointments.size());
        model.addAttribute("totalDoctors", allDoctors.size());
        model.addAttribute("totalPatients", allPatients.size());
        model.addAttribute("pendingAppointments", 
            allAppointments.stream().filter(a -> a.getStatus() == Appointment.AppointmentStatus.PENDING).count());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/appointments")
    public String viewAllAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "admin/appointments";
    }
    
    @GetMapping("/doctors")
    public String viewAllDoctors(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "admin/doctors";
    }
    
    @GetMapping("/patients")
    public String viewAllPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        return "admin/patients";
    }
    
    @PostMapping("/appointments/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, 
                                        @RequestParam Appointment.AppointmentStatus status) {
        appointmentService.updateAppointmentStatus(id, status);
        return "redirect:/admin/appointments";
    }
    
    @PostMapping("/appointments/{id}/delete")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/admin/appointments";
    }
    
    
    @GetMapping("/patients/{id}/edit")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "admin/edit-patient";
    }
    
    @PostMapping("/patients/{id}/edit")
    public String updatePatient(@PathVariable Long id, 
                               @ModelAttribute Patient patient,
                               RedirectAttributes redirectAttributes) {
        try {
            Patient existingPatient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            
           
            existingPatient.setName(patient.getName());
            existingPatient.setEmail(patient.getEmail());
            existingPatient.setPhone(patient.getPhone());
            existingPatient.setDateOfBirth(patient.getDateOfBirth());
            existingPatient.setGender(patient.getGender());
            existingPatient.setAddress(patient.getAddress());
            
            patientRepository.save(existingPatient);
            
           
            User user = userRepository.findByEmail(existingPatient.getEmail())
                    .orElse(null);
            if (user != null) {
                user.setFullName(patient.getName());
                user.setEmail(patient.getEmail());
                userRepository.save(user);
            }
            
            redirectAttributes.addFlashAttribute("success", "Patient updated successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update patient: " + e.getMessage());
        }
        
        return "redirect:/admin/patients";
    }
    
    @PostMapping("/patients/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            
            String patientEmail = patient.getEmail();
            System.out.println("Attempting to delete patient: " + patient.getName() + " (Email: " + patientEmail + ")");
            
            
            User user = userRepository.findByEmail(patientEmail)
                    .orElse(null);
            if (user != null) {
                System.out.println("Found user record for email: " + patientEmail);
                userRepository.delete(user);
                System.out.println("Successfully deleted user with email: " + patientEmail);
            } else {
                System.out.println("No user record found for email: " + patientEmail);
            }
            
         
            patientRepository.deleteById(id);
            System.out.println("Successfully deleted patient with ID: " + id);
            
            redirectAttributes.addFlashAttribute("success", "Patient and associated user deleted successfully!");
        } catch (Exception e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete patient: " + e.getMessage());
        }
        return "redirect:/admin/patients";
    }
    
    
    @PostMapping("/cleanup-users")
    public String cleanupOrphanedUsers(RedirectAttributes redirectAttributes) {
        try {
            List<User> allUsers = userRepository.findAll();
            int deletedCount = 0;
            
            for (User user : allUsers) {
                if (user.getRole() == User.UserRole.PATIENT) {
                    // Check if patient exists
                    Optional<Patient> patient = patientRepository.findByEmail(user.getEmail());
                    if (patient.isEmpty()) {
                        userRepository.delete(user);
                        deletedCount++;
                        System.out.println("Deleted orphaned user: " + user.getEmail());
                    }
                }
            }
            
            redirectAttributes.addFlashAttribute("success", 
                "Cleanup completed! Deleted " + deletedCount + " orphaned user records.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cleanup failed: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @PostMapping("/doctors/add")
    public String addDoctor(@RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String specialization,
                            @RequestParam String qualification,
                            RedirectAttributes redirectAttributes) {
        try {
            // 1. Doctor entity create & save
            Doctor doctor = new Doctor();
            doctor.setName(name);
            doctor.setEmail(email);
            doctor.setPhone(phone);
            doctor.setSpecialization(specialization);
            doctor.setQualification(qualification);
            doctor.setActive(true);
            doctorRepository.save(doctor);

           
            if (!userRepository.existsByEmail(email)) {
                User user = new User();
                user.setUsername(email);
                user.setEmail(email);
               
                user.setPassword("temp1234");
                user.setFullName(name);
                user.setRole(User.UserRole.DOCTOR);
                user.setActive(true);
                userRepository.save(user);
            }

          
            passwordResetService.requestPasswordReset(email);

            redirectAttributes.addFlashAttribute("success", "Doctor added successfully! Login instructions sent to email.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add doctor: " + e.getMessage());
        }
        return "redirect:/admin/doctors";
    }

    @GetMapping("/doctors/{id}/edit")
    public String showEditDoctorForm(@PathVariable Long id, Model model) {
        var doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "admin/edit-doctor";
    }

    @PostMapping("/doctors/{id}/edit")
    public String updateDoctor(@PathVariable Long id, @ModelAttribute Doctor doctor) {
        doctor.setId(id);
        doctorRepository.save(doctor);
        return "redirect:/admin/doctors";
    }

    @PostMapping("/doctors/{id}/delete")
    public String deleteDoctor(@PathVariable Long id) {
        doctorRepository.deleteById(id);
        return "redirect:/admin/doctors";
    }
} 