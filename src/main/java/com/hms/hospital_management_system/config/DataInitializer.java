package com.hms.hospital_management_system.config;

import com.hms.hospital_management_system.entity.User;
import com.hms.hospital_management_system.entity.Patient;
import com.hms.hospital_management_system.repository.UserRepository;
import com.hms.hospital_management_system.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
      
        createDemoUsers();
    }

    private void createDemoUsers() {
        
        if (!userRepository.findByEmail("admin@hospital.com").isPresent()) {
            User admin = new User();
            admin.setUsername("admin@hospital.com");
            admin.setEmail("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setRole(User.UserRole.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Created admin user: admin@hospital.com");
        }

       
        if (!userRepository.findByEmail("doctor@hospital.com").isPresent()) {
            User doctor = new User();
            doctor.setUsername("doctor@hospital.com");
            doctor.setEmail("doctor@hospital.com");
            doctor.setPassword(passwordEncoder.encode("doctor123"));
            doctor.setFullName("Dr. John Smith");
            doctor.setRole(User.UserRole.DOCTOR);
            doctor.setActive(true);
            userRepository.save(doctor);
            System.out.println("Created doctor user: doctor@hospital.com");
        }

       
        if (!userRepository.findByEmail("patient@example.com").isPresent()) {
            User patient = new User();
            patient.setUsername("patient@example.com");
            patient.setEmail("patient@example.com");
            patient.setPassword(passwordEncoder.encode("patient123"));
            patient.setFullName("John Doe");
            patient.setRole(User.UserRole.PATIENT);
            patient.setActive(true);
            userRepository.save(patient);
            System.out.println("Created patient user: patient@example.com");
        }
        
        if (!userRepository.findByEmail("receptionist@hospital.com").isPresent()) {
            User receptionist = new User();
            receptionist.setUsername("receptionist@hospital.com");
            receptionist.setEmail("receptionist@hospital.com");
            receptionist.setPassword(passwordEncoder.encode("receptionist123"));
            receptionist.setFullName("Jane Wilson");
            receptionist.setRole(User.UserRole.RECEPTIONIST);
            receptionist.setActive(true);
            userRepository.save(receptionist);
            System.out.println("Created receptionist user: receptionist@hospital.com");
        }
        
  
        cleanupOrphanedUsers();
    }
    
    private void cleanupOrphanedUsers() {
        try {
            List<User> allUsers = userRepository.findAll();
            int deletedCount = 0;
            
            for (User user : allUsers) {
                if (user.getRole() == User.UserRole.PATIENT) {
                   
                    Optional<Patient> patient = patientRepository.findByEmail(user.getEmail());
                    if (patient.isEmpty()) {
                        userRepository.delete(user);
                        deletedCount++;
                        System.out.println("Cleaned up orphaned user: " + user.getEmail());
                    }
                }
            }
            
            if (deletedCount > 0) {
                System.out.println("Cleanup completed! Deleted " + deletedCount + " orphaned user records.");
            }
        } catch (Exception e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
} 