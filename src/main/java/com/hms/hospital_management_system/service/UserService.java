package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.dto.RegistrationRequest;

import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.entity.User;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Provider;
import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final Provider<PasswordEncoder> passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PatientRepository patientRepository, Provider<PasswordEncoder> passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    @Transactional
    public void registerNewPatient(RegistrationRequest registrationRequest) {
     
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.get().encode(registrationRequest.getPassword()));
        user.setFullName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
        user.setRole(User.UserRole.PATIENT);
        user.setActive(true);
        user.setUsername(registrationRequest.getEmail());
        userRepository.save(user);

       
        Patient patient = new Patient();
        patient.setName(registrationRequest.getFirstName() + " " + registrationRequest.getLastName());
        patient.setEmail(registrationRequest.getEmail());
        patient.setPhone(registrationRequest.getPhone());
        patient.setDateOfBirth(registrationRequest.getDateOfBirth());
        patient.setGender(registrationRequest.getGender());
        patient.setAddress(registrationRequest.getAddress());
        patient.setEmergencyContact(registrationRequest.getEmergencyContact());
        patient.setMedicalHistory(registrationRequest.getMedicalHistory());
        patientRepository.save(patient);
    }
} 