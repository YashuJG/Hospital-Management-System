package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.MedicalRecordRequest;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.MedicalRecord;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.MedicalRecordRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    public MedicalRecord createMedicalRecord(MedicalRecordRequest request) {
        Optional<Patient> patientOpt = patientRepository.findById(request.getPatientId());
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
        
        if (patientOpt.isEmpty()) {
            throw new RuntimeException("Patient not found with ID: " + request.getPatientId());
        }
        
        if (doctorOpt.isEmpty()) {
            throw new RuntimeException("Doctor not found with ID: " + request.getDoctorId());
        }
        
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patientOpt.get());
        medicalRecord.setDoctor(doctorOpt.get());
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setSymptoms(request.getSymptoms());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setNotes(request.getNotes());
        medicalRecord.setVisitDate(request.getVisitDate());
        medicalRecord.setNextVisitDate(request.getNextVisitDate());
        medicalRecord.setBloodPressure(request.getBloodPressure());
        medicalRecord.setTemperature(request.getTemperature());
        medicalRecord.setWeight(request.getWeight());
        medicalRecord.setHeight(request.getHeight());
        medicalRecord.setPulseRate(request.getPulseRate());
        medicalRecord.setLabTests(request.getLabTests());
        medicalRecord.setTestResults(request.getTestResults());
        medicalRecord.setMedicationsPrescribed(request.getMedicationsPrescribed());
        medicalRecord.setDosageInstructions(request.getDosageInstructions());
        
        return medicalRecordRepository.save(medicalRecord);
    }
    
    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patientId);
    }
    
    public List<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctorIdOrderByVisitDateDesc(doctorId);
    }
    
    public List<MedicalRecord> getMedicalRecordsByPatientAndDoctor(Long patientId, Long doctorId) {
        return medicalRecordRepository.findByPatientIdAndDoctorIdOrderByVisitDateDesc(patientId, doctorId);
    }
    
    public List<MedicalRecord> getMedicalRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return medicalRecordRepository.findByVisitDateBetween(startDate, endDate);
    }
    
    public List<MedicalRecord> searchMedicalRecordsByDiagnosis(Long patientId, String diagnosis) {
        return medicalRecordRepository.findByPatientIdAndDiagnosisContaining(patientId, diagnosis);
    }
    
    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }
    
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecordRequest request) {
        Optional<MedicalRecord> existingRecord = medicalRecordRepository.findById(id);
        
        if (existingRecord.isEmpty()) {
            throw new RuntimeException("Medical record not found with ID: " + id);
        }
        
        MedicalRecord medicalRecord = existingRecord.get();
        medicalRecord.setDiagnosis(request.getDiagnosis());
        medicalRecord.setSymptoms(request.getSymptoms());
        medicalRecord.setTreatment(request.getTreatment());
        medicalRecord.setNotes(request.getNotes());
        medicalRecord.setVisitDate(request.getVisitDate());
        medicalRecord.setNextVisitDate(request.getNextVisitDate());
        medicalRecord.setBloodPressure(request.getBloodPressure());
        medicalRecord.setTemperature(request.getTemperature());
        medicalRecord.setWeight(request.getWeight());
        medicalRecord.setHeight(request.getHeight());
        medicalRecord.setPulseRate(request.getPulseRate());
        medicalRecord.setLabTests(request.getLabTests());
        medicalRecord.setTestResults(request.getTestResults());
        medicalRecord.setMedicationsPrescribed(request.getMedicationsPrescribed());
        medicalRecord.setDosageInstructions(request.getDosageInstructions());
        
        return medicalRecordRepository.save(medicalRecord);
    }
    
    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new RuntimeException("Medical record not found with ID: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
    
    public Long getDoctorRecordCount(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        return medicalRecordRepository.countByDoctorAndDateRange(doctorId, startDate, endDate);
    }
    
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }
    
    public List<MedicalRecord> getMedicalRecordsByFilters(Long patientId, String diagnosis, LocalDateTime fromDate, LocalDateTime toDate) {
        if (patientId != null && diagnosis != null && !diagnosis.trim().isEmpty()) {
            return medicalRecordRepository.findByPatientIdAndDiagnosisAndDateRange(patientId, diagnosis, fromDate, toDate);
        } else if (patientId != null) {
            return medicalRecordRepository.findByPatientIdAndDateRange(patientId, fromDate, toDate);
        } else if (diagnosis != null && !diagnosis.trim().isEmpty()) {
            return medicalRecordRepository.findByDiagnosisAndDateRange(diagnosis, fromDate, toDate);
        } else {
            return medicalRecordRepository.findByVisitDateBetween(fromDate, toDate);
        }
    }
} 