package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.UserRepository;
import com.hms.hospital_management_system.service.PasswordResetService;
import com.hms.hospital_management_system.dto.RegistrationRequest;
import com.hms.hospital_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.Period;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
     
        long totalAppointments = appointmentRepository.count();
        long pendingAppointments = appointmentRepository.countByStatus(Appointment.AppointmentStatus.PENDING);
        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        
        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("pendingAppointments", pendingAppointments);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalDoctors", totalDoctors);
        
        return "receptionist/dashboard";
    }

    @PostMapping("/appointments/{id}/status")
    public String updateAppointmentStatus(@PathVariable Long id, @RequestParam String status) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment != null) {
            appointment.setStatus(Appointment.AppointmentStatus.valueOf(status));
            appointmentRepository.save(appointment);
        }
        return "redirect:/receptionist/appointments";
    }

//    @GetMapping("/patients")
//     public String patients(Model model) {
//         List<Patient> patients = patientRepository.findAll();
//         model.addAttribute("patients", patients);
//         return "receptionist/patients";
//     }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "receptionist/doctors";
    }

    @GetMapping("/doctors/{id}/schedule")
    public String doctorSchedule(@PathVariable Long id, @RequestParam(required = false) String date, Model model) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor == null) {
            return "redirect:/receptionist/doctors";
        }
        // Date selection (default: today)
        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        // Fetch all appointments for doctor on selected date
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        // Filter only for selected date
        Set<LocalTime> bookedTimes = new HashSet<>();
        for (Appointment appt : appointments) {
            if (appt.getAppointmentDateTime().toLocalDate().equals(selectedDate)) {
                bookedTimes.add(appt.getAppointmentDateTime().toLocalTime().withMinute(0).withSecond(0).withNano(0));
            }
        }
        // Working hours: 9AM to 5PM
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);
        List<LocalTime> availableSlots = new ArrayList<>();
        for (LocalTime time = start; time.isBefore(end); time = time.plusHours(1)) {
            if (!bookedTimes.contains(time)) {
                availableSlots.add(time);
            }
        }
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("availableSlots", availableSlots);
        return "receptionist/doctor-schedule";
    }
} 