package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Doctor;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }
    
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        return patient.map(appointmentRepository::findByPatient).orElse(List.of());
    }
    
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        return doctor.map(appointmentRepository::findByDoctor).orElse(List.of());
    }
    
    public List<Appointment> getAppointmentsByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }
    
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }
    
    public Appointment createAppointment(Appointment appointment) {
       
        if (appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment time cannot be in the past");
        }
        
      
        List<Appointment> conflictingAppointments = appointmentRepository
            .findByDoctorAndAppointmentDateTimeBetween(
                appointment.getDoctor(),
                appointment.getAppointmentDateTime().minusMinutes(30),
                appointment.getAppointmentDateTime().plusMinutes(30)
            );
        
        if (!conflictingAppointments.isEmpty()) {
            throw new IllegalArgumentException("Doctor is not available at this time");
        }
        
        return appointmentRepository.save(appointment);
    }
    
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        
        if (existingAppointment.isPresent()) {
            Appointment appointment = existingAppointment.get();
            appointment.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
            appointment.setStatus(appointmentDetails.getStatus());
            appointment.setReason(appointmentDetails.getReason());
            appointment.setNotes(appointmentDetails.getNotes());
            appointment.setUpdatedAt(LocalDateTime.now());
            
            return appointmentRepository.save(appointment);
        }
        
        throw new IllegalArgumentException("Appointment not found with id: " + id);
    }
    
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Optional<Appointment> existingAppointment = appointmentRepository.findById(id);
        
        if (existingAppointment.isPresent()) {
            Appointment appointment = existingAppointment.get();
            appointment.setStatus(status);
            appointment.setUpdatedAt(LocalDateTime.now());
            
            return appointmentRepository.save(appointment);
        }
        
        throw new IllegalArgumentException("Appointment not found with id: " + id);
    }
    
    public void deleteAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        
        if (appointment.isPresent()) {
            appointmentRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Appointment not found with id: " + id);
        }
    }
    
    public boolean isDoctorAvailable(Long doctorId, LocalDateTime appointmentTime) {
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        
        if (doctor.isPresent()) {
            List<Appointment> conflictingAppointments = appointmentRepository
                .findByDoctorAndAppointmentDateTimeBetween(
                    doctor.get(),
                    appointmentTime.minusMinutes(30),
                    appointmentTime.plusMinutes(30)
                );
            
            return conflictingAppointments.isEmpty();
        }
        
        return false;
    }
} 