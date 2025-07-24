package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.PrescriptionRequest;
import com.hms.hospital_management_system.entity.Appointment;
import com.hms.hospital_management_system.entity.Prescription;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import com.hms.hospital_management_system.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public List<Appointment> getCompletedAppointments() {
        return appointmentRepository.findByStatus(Appointment.AppointmentStatus.COMPLETED);
    }

    public Prescription createPrescription(PrescriptionRequest prescriptionRequest) {
        Appointment appointment = appointmentRepository.findById(prescriptionRequest.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDiagnosis(prescriptionRequest.getDiagnosis());
        prescription.setMedications(prescriptionRequest.getMedications());
        prescription.setInstructions(prescriptionRequest.getInstructions());
        prescription.setPrescribedAt(prescriptionRequest.getPrescribedAt());

        return prescriptionRepository.save(prescription);
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }
}
