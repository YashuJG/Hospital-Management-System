package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/receptionist/appointments")
public class ReceptionistAppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Show new appointment form
    @GetMapping("/new")
    public String showNewAppointmentForm(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("patients", patients);
        model.addAttribute("appointment", new Appointment());
        return "receptionist/new-appointment";
    }

    // Handle new appointment submission
    @PostMapping
    public String createAppointment(@ModelAttribute Appointment appointment, @RequestParam Long doctorId, @RequestParam Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(Appointment.AppointmentStatus.PENDING); // or CONFIRMED as per your logic
        appointmentRepository.save(appointment);
        return "redirect:/receptionist/appointments";
    }

    // List all appointments (optional)
    @GetMapping
    public String listAppointments(Model model) {
        List<Appointment> appointments = appointmentRepository.findAll();
        model.addAttribute("appointments", appointments);
        return "receptionist/appointments";
    }
} 