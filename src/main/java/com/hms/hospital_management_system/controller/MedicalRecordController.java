package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.MedicalRecordRequest;
import com.hms.hospital_management_system.entity.MedicalRecord;
import com.hms.hospital_management_system.service.MedicalRecordService;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/medical-records")
public class MedicalRecordController {
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @GetMapping
    public String listMedicalRecords(Model model) {
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByDateRange(
            LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        model.addAttribute("medicalRecords", records);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("fromDate", LocalDateTime.now().minusMonths(1));
        model.addAttribute("toDate", LocalDateTime.now());
        return "medical-records/list";
    }
    
    @GetMapping("/create")
    public String createMedicalRecordForm(Model model) {
        model.addAttribute("medicalRecordRequest", new MedicalRecordRequest());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("doctors", doctorRepository.findAll());
        return "medical-records/create";
    }
    
    @PostMapping("/create")
    public String createMedicalRecord(@Valid @ModelAttribute MedicalRecordRequest request,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "medical-records/create";
        }
        
        try {
            medicalRecordService.createMedicalRecord(request);
            redirectAttributes.addFlashAttribute("success", "Medical record created successfully!");
            return "redirect:/medical-records";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create medical record: " + e.getMessage());
            return "redirect:/medical-records/create";
        }
    }
    
    @GetMapping("/{id}")
    public String viewMedicalRecord(@PathVariable Long id, Model model) {
        return medicalRecordService.getMedicalRecordById(id)
                .map(record -> {
                    model.addAttribute("medicalRecord", record);
                    return "medical-records/view";
                })
                .orElse("redirect:/medical-records");
    }
    
    @GetMapping("/{id}/edit")
    public String editMedicalRecordForm(@PathVariable Long id, Model model) {
        return medicalRecordService.getMedicalRecordById(id)
                .map(record -> {
                    MedicalRecordRequest request = new MedicalRecordRequest();
                    request.setPatientId(record.getPatient().getId());
                    request.setDoctorId(record.getDoctor().getId());
                    request.setDiagnosis(record.getDiagnosis());
                    request.setSymptoms(record.getSymptoms());
                    request.setTreatment(record.getTreatment());
                    request.setNotes(record.getNotes());
                    request.setVisitDate(record.getVisitDate());
                    request.setNextVisitDate(record.getNextVisitDate());
                    request.setBloodPressure(record.getBloodPressure());
                    request.setTemperature(record.getTemperature());
                    request.setWeight(record.getWeight());
                    request.setHeight(record.getHeight());
                    request.setPulseRate(record.getPulseRate());
                    request.setLabTests(record.getLabTests());
                    request.setTestResults(record.getTestResults());
                    request.setMedicationsPrescribed(record.getMedicationsPrescribed());
                    request.setDosageInstructions(record.getDosageInstructions());
                    
                    model.addAttribute("medicalRecordRequest", request);
                    model.addAttribute("recordId", id);
                    return "medical-records/edit";
                })
                .orElse("redirect:/medical-records");
    }
    
    @PostMapping("/{id}/edit")
    public String updateMedicalRecord(@PathVariable Long id,
                                     @Valid @ModelAttribute MedicalRecordRequest request,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "medical-records/edit";
        }
        
        try {
            medicalRecordService.updateMedicalRecord(id, request);
            redirectAttributes.addFlashAttribute("success", "Medical record updated successfully!");
            return "redirect:/medical-records/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update medical record: " + e.getMessage());
            return "redirect:/medical-records/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteMedicalRecord(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            medicalRecordService.deleteMedicalRecord(id);
            redirectAttributes.addFlashAttribute("success", "Medical record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete medical record: " + e.getMessage());
        }
        return "redirect:/medical-records";
    }
    
    @GetMapping("/patient/{patientId}")
    public String getPatientMedicalRecords(@PathVariable Long patientId, Model model) {
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        model.addAttribute("medicalRecords", records);
        model.addAttribute("patientId", patientId);
        return "medical-records/patient-records";
    }
    
    @GetMapping("/doctor/{doctorId}")
    public String getDoctorMedicalRecords(@PathVariable Long doctorId, Model model) {
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByDoctor(doctorId);
        model.addAttribute("medicalRecords", records);
        model.addAttribute("doctorId", doctorId);
        return "medical-records/doctor-records";
    }
    
    @GetMapping("/search")
    public String searchMedicalRecords(@RequestParam(required = false) Long patientId,
                                      @RequestParam(required = false) String diagnosis,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
                                      Model model) {
        List<MedicalRecord> records;
        if (fromDate != null && toDate != null) {
            records = medicalRecordService.getMedicalRecordsByFilters(patientId, diagnosis, fromDate, toDate);
        } else if (patientId != null && (diagnosis != null && !diagnosis.trim().isEmpty())) {
            records = medicalRecordService.searchMedicalRecordsByDiagnosis(patientId, diagnosis);
        } else if (patientId != null) {
            records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        } else {
            records = medicalRecordService.getAllMedicalRecords();
        }
        model.addAttribute("medicalRecords", records);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("patientId", patientId);
        model.addAttribute("searchDiagnosis", diagnosis);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        return "medical-records/list";
    }
} 