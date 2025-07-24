package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.User;
import com.hms.hospital_management_system.service.AppointmentService;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Validator;
import org.springframework.validation.SmartValidator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SmartValidator smartValidator;
    
    @GetMapping("/book")
    public String showBookingForm(Model model) {
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        
      
        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        if (!patientOpt.isPresent()) {
            return "redirect:/login?error=Patient not found. Please login with your patient account.";
        }
        
        Patient currentPatient = patientOpt.get();
        List<Doctor> doctors = doctorRepository.findByActiveTrue();
        
        model.addAttribute("doctors", doctors);
        model.addAttribute("currentPatient", currentPatient);
        model.addAttribute("appointment", new Appointment());
        
        return "appointments/book";
    }
    
    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment,
                                 BindingResult result,
                                 @RequestParam("doctorId") Long doctorId,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("appointmentDateTime") LocalDateTime appointmentDateTime,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Optional<Patient> patientOpt = patientRepository.findByEmail(userEmail);
        if (patientOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Patient not found. Please login again.");
            return "redirect:/login";
        }

        Patient currentPatient = patientOpt.get();

       
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isPresent()) {
            appointment.setDoctor(doctorOpt.get());
        }

      
        appointment.setPatient(currentPatient);
        appointment.setAppointmentDateTime(appointmentDateTime);
        
        
        smartValidator.validate(appointment, result);

        if (result.hasErrors()) {
            
            result.getAllErrors().forEach(error -> logger.error("Validation Error: {}", error.getDefaultMessage()));
            
            model.addAttribute("error", "Please correct the errors below.");
            model.addAttribute("doctors", doctorRepository.findByActiveTrue());
            model.addAttribute("currentPatient", currentPatient);
            return "appointments/book";
        }

        try {
            appointmentService.createAppointment(appointment);

            redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
            return "redirect:/appointments/success";

        } catch (Exception e) {
            logger.error("Could not book appointment", e);
            model.addAttribute("error", "Could not book appointment: " + e.getMessage());
            model.addAttribute("doctors", doctorRepository.findByActiveTrue());
            model.addAttribute("currentPatient", currentPatient);
            return "appointments/book";
        }
    }
    
    @GetMapping("/success")
    public String bookingSuccess() {
        return "appointments/success";
    }
    
    @GetMapping("/list")
    public String listAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments/list";
    }
} 