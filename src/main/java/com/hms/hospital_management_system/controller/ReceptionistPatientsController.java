package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receptionist/patients")
public class ReceptionistPatientsController {

    @Autowired
    private PatientRepository patientRepository;

    // List all patients
    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientRepository.findAll());
        return "receptionist/patients";
    }

    // Add new patient (POST)
    @PostMapping("/add")
    public String addPatient(@ModelAttribute Patient patient) {
        patientRepository.save(patient);
        return "redirect:/receptionist/patients";
    }

    // View patient details
    @GetMapping("/{id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "receptionist/view-patient";
    }

    // Edit patient form
    @GetMapping("/{id}/edit")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "receptionist/edit-patient";
    }

    // Update patient
    @PostMapping("/{id}/edit")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient patient) {
        // Get the existing patient to preserve values that might not be in the form
        Patient existingPatient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        // Update only the fields that are provided in the form
        if (patient.getName() != null && !patient.getName().trim().isEmpty()) {
            existingPatient.setName(patient.getName());
        }
        if (patient.getEmail() != null && !patient.getEmail().trim().isEmpty()) {
            existingPatient.setEmail(patient.getEmail());
        }
        if (patient.getPhone() != null && !patient.getPhone().trim().isEmpty()) {
            existingPatient.setPhone(patient.getPhone());
        }
        if (patient.getAddress() != null && !patient.getAddress().trim().isEmpty()) {
            existingPatient.setAddress(patient.getAddress());
        }
        if (patient.getDateOfBirth() != null) {
            existingPatient.setDateOfBirth(patient.getDateOfBirth());
        }
        if (patient.getGender() != null && !patient.getGender().trim().isEmpty()) {
            existingPatient.setGender(patient.getGender());
        }
        if (patient.getEmergencyContact() != null) {
            existingPatient.setEmergencyContact(patient.getEmergencyContact());
        }
        if (patient.getMedicalHistory() != null) {
            existingPatient.setMedicalHistory(patient.getMedicalHistory());
        }
        
        patientRepository.save(existingPatient);
        return "redirect:/receptionist/patients";
    }

    // Delete patient
    @PostMapping("/{id}/delete")
    public String deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
        return "redirect:/receptionist/patients";
    }
} 