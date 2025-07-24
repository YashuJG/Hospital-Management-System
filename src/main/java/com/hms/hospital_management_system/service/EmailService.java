package com.hms.hospital_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class EmailService {
    
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());
    
    @Autowired
    private JavaMailSender mailSender;
    
    
    public boolean testEmailConfiguration() {
        try {
            logger.info("Testing email configuration...");
            
            SimpleMailMessage testMessage = new SimpleMailMessage();
            testMessage.setTo("yasaswinisrit@gmail.com"); 
            testMessage.setSubject("Email Configuration Test - Hospital Management System");
            testMessage.setText(
                "This is a test email to verify that the email configuration is working properly.\n\n" +
                "If you receive this email, the email service is configured correctly.\n\n" +
                "Timestamp: " + java.time.LocalDateTime.now() + "\n\n" +
                "Best regards,\n" +
                "Hospital Management System"
            );
            
            mailSender.send(testMessage);
            logger.info("Test email sent successfully!");
            System.out.println("=== EMAIL CONFIGURATION TEST SUCCESSFUL ===");
            System.out.println("Test email sent to: yasaswinisrit@gmail.com");
            System.out.println("===========================================");
            return true;
            
        } catch (Exception e) {
            logger.severe("Email configuration test failed: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== EMAIL CONFIGURATION TEST FAILED ===");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please check your email configuration in application.properties");
            System.out.println("=======================================");
            return false;
        }
    }
    
    public void sendAppointmentConfirmation(String to, String patientName, String doctorName, String appointmentDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Appointment Confirmation - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Your appointment with Dr. " + doctorName + " has been confirmed for " + appointmentDate + ".\n\n" +
                "Please arrive 15 minutes before your scheduled time.\n" +
                "Don't forget to bring your medical records and ID.\n\n" +
                "If you need to reschedule, please contact us at least 24 hours in advance.\n\n" +
                "Thank you for choosing our Hospital Management System.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Appointment confirmation email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send appointment confirmation email to " + to + ": " + e.getMessage());
            e.printStackTrace();
           
            System.out.println("=== APPOINTMENT CONFIRMATION EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Appointment Confirmation");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your appointment with Dr. " + doctorName + " has been confirmed for " + appointmentDate);
            System.out.println("Please arrive 15 minutes before your scheduled time.");
            System.out.println("Thank you for choosing our hospital.");
            System.out.println("=====================================");
        }
    }
    
    public void sendAppointmentReminder(String to, String patientName, String doctorName, String appointmentDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Appointment Reminder - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "This is a friendly reminder for your appointment with Dr. " + doctorName + " on " + appointmentDate + ".\n\n" +
                "Please don't forget to bring:\n" +
                "- Your medical records\n" +
                "- Photo ID\n" +
                "- Insurance card (if applicable)\n" +
                "- List of current medications\n\n" +
                "If you need to reschedule, please contact us immediately.\n\n" +
                "Thank you,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Appointment reminder email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send appointment reminder email to " + to + ": " + e.getMessage());
            e.printStackTrace();
          
            System.out.println("=== APPOINTMENT REMINDER EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Appointment Reminder");
            System.out.println("Dear " + patientName + ",");
            System.out.println("This is a reminder for your appointment with Dr. " + doctorName + " on " + appointmentDate);
            System.out.println("Please don't forget to bring your medical records.");
            System.out.println("=====================================");
        }
    }
    
    public void sendPrescriptionNotification(String to, String patientName, String doctorName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("New Prescription Available - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Dr. " + doctorName + " has prescribed new medications for you.\n\n" +
                "Please log in to your patient portal to view the complete prescription details.\n\n" +
                "Important reminders:\n" +
                "- Take medications as prescribed\n" +
                "- Follow dosage instructions carefully\n" +
                "- Contact your doctor if you experience any side effects\n\n" +
                "Thank you,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Prescription notification email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send prescription notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
          
            System.out.println("=== PRESCRIPTION NOTIFICATION EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: New Prescription Available");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Dr. " + doctorName + " has prescribed new medications for you.");
            System.out.println("Please check your patient portal for details.");
            System.out.println("=====================================");
        }
    }
    
    public void sendRegistrationConfirmation(String to, String patientName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Welcome to our Hospital Management System!\n\n" +
                "Your account has been successfully created. You can now:\n" +
                "- Book appointments with our doctors\n" +
                "- Access your medical records\n" +
                "- View prescriptions and invoices\n" +
                "- Receive appointment reminders\n\n" +
                "To get started, please log in to your account at: http://localhost:8080/login\n\n" +
                "If you have any questions, please don't hesitate to contact us.\n\n" +
                "Thank you for choosing our hospital.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Registration confirmation email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send registration confirmation email to " + to + ": " + e.getMessage());
            e.printStackTrace();
          
            System.out.println("=== REGISTRATION CONFIRMATION EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Welcome to Hospital Management System");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Welcome to our Hospital Management System!");
            System.out.println("Your account has been successfully created.");
            System.out.println("You can now book appointments and access your medical records.");
            System.out.println("=====================================");
        }
    }
    
    public void sendPasswordResetEmail(String to, String userName, String resetToken) {
        String resetLink = "http://localhost:8095/reset-password?token=" + resetToken;
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Password Reset Request - Hospital Management System");
            message.setText(
                "Dear " + userName + ",\n\n" +
                "You have requested to reset your password for your Hospital Management System account.\n\n" +
                "To reset your password, please click the following link:\n" +
                resetLink + "\n\n" +
                "Important Information:\n" +
                "- This link will expire in 1 hour\n" +
                "- If you didn't request this password reset, please ignore this email\n" +
                "- For security reasons, please don't share this link with anyone\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Thank you for using our Hospital Management System.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: " + to);
            System.out.println("=== PASSWORD RESET EMAIL SENT SUCCESSFULLY ===");
            System.out.println("To: " + to);
            System.out.println("Reset Link: " + resetLink);
            System.out.println("=============================================");
            
        } catch (Exception e) {
            logger.severe("Failed to send password reset email to " + to + ": " + e.getMessage());
            e.printStackTrace();
          
            System.out.println("=== PASSWORD RESET EMAIL (FALLBACK) ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Password Reset Request");
            System.out.println("Dear " + userName + ",");
            System.out.println("You have requested to reset your password.");
            System.out.println("Click the following link to reset your password:");
            System.out.println("Reset Link: " + resetLink);
            System.out.println("This link will expire in 1 hour.");
            System.out.println("If you didn't request this, please ignore this email.");
            System.out.println("Thank you for using our Hospital Management System.");
            System.out.println("=====================================");
        }
    }

   
    public void sendBillCreatedNotification(String to, String patientName, String billNumber, String amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("New Bill Generated - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "A new bill has been generated for you.\n\n" +
                "Bill Details:\n" +
                "- Bill Number: " + billNumber + "\n" +
                "- Amount: ₹" + amount + "\n\n" +
                "Please log in to your patient portal to view and pay your bill.\n\n" +
                "Thank you,\n" +
                "Hospital Management Team"
            );
            mailSender.send(message);
            logger.info("Bill created notification email sent successfully to: " + to);
        } catch (Exception e) {
            logger.severe("Failed to send bill created notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== BILL CREATED EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: New Bill Generated");
            System.out.println("Dear " + patientName + ",");
            System.out.println("A new bill (Bill #" + billNumber + ") has been generated for ₹" + amount);
            System.out.println("Please check your patient portal for payment details.");
            System.out.println("=====================================");
        }
    }

    
    public void sendBillPaidNotification(String to, String patientName, String billNumber, String amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bill Paid Confirmation - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Your bill has been paid successfully.\n\n" +
                "Bill Details:\n" +
                "- Bill Number: " + billNumber + "\n" +
                "- Amount Paid: ₹" + amount + "\n\n" +
                "Thank you for your payment!\n\n" +
                "Hospital Management Team"
            );
            mailSender.send(message);
            logger.info("Bill paid notification email sent successfully to: " + to);
        } catch (Exception e) {
            logger.severe("Failed to send bill paid notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== BILL PAID EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Bill Paid Confirmation");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your bill (Bill #" + billNumber + ") has been paid for ₹" + amount);
            System.out.println("Thank you for your payment!");
            System.out.println("=====================================");
        }
    }
    
   
    public void sendPaymentSubmittedNotification(String to, String patientName, String billNumber, String amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Payment Submitted - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Your payment of ₹" + amount + " for Bill #" + billNumber + " has been submitted successfully.\n\n" +
                "Your payment is currently pending confirmation from our administrative team.\n" +
                "You will receive another email once your payment is confirmed.\n\n" +
                "Payment Details:\n" +
                "- Bill Number: " + billNumber + "\n" +
                "- Amount Submitted: ₹" + amount + "\n" +
                "- Submission Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                "Please note: This payment will be reviewed and confirmed within 24 hours.\n\n" +
                "Thank you for your patience.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Payment submitted notification email sent successfully to: " + to);
            System.out.println("=== PAYMENT SUBMITTED EMAIL SENT SUCCESSFULLY ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Payment Submitted");
            System.out.println("Patient: " + patientName);
            System.out.println("Amount: ₹" + amount + " for Bill #" + billNumber);
            System.out.println("=====================================");
            
        } catch (Exception e) {
            logger.severe("Failed to send payment submitted notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== PAYMENT SUBMITTED NOTIFICATION EMAIL (FALLBACK) ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Payment Submitted");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your payment of ₹" + amount + " for Bill #" + billNumber + " has been submitted.");
            System.out.println("Payment is pending confirmation from administrative team.");
            System.out.println("Error: " + e.getMessage());
            System.out.println("=====================================");
        }
    }
    
   
    public void sendPaymentConfirmationRequest(com.hms.hospital_management_system.entity.Bill bill) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("admin@hospital.com"); 
            message.setSubject("Payment Confirmation Required - Bill #" + bill.getBillNumber());
            message.setText(
                "A new payment requires confirmation:\n\n" +
                "Patient Details:\n" +
                "- Name: " + bill.getPatient().getName() + "\n" +
                "- Email: " + bill.getPatient().getEmail() + "\n" +
                "- Phone: " + bill.getPatient().getPhone() + "\n\n" +
                "Bill Details:\n" +
                "- Bill Number: " + bill.getBillNumber() + "\n" +
                "- Total Amount: ₹" + bill.getTotalAmount() + "\n" +
                "- Paid Amount: ₹" + bill.getPaidAmount() + "\n" +
                "- Payment Method: " + bill.getPaymentMethod() + "\n" +
                "- Payment Date: " + bill.getPaymentDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                "Please review and confirm this payment in the admin panel.\n\n" +
                "Bill ID: " + bill.getId() + "\n" +
                "Confirmation Link: http://localhost:8095/bills/" + bill.getId() + "\n\n" +
                "Best regards,\n" +
                "Hospital Management System"
            );
            
            mailSender.send(message);
            logger.info("Payment confirmation request email sent successfully to admin");
            System.out.println("=== PAYMENT CONFIRMATION EMAIL SENT SUCCESSFULLY ===");
            System.out.println("To: admin@hospital.com");
            System.out.println("Subject: Payment Confirmation Required - Bill #" + bill.getBillNumber());
            System.out.println("Patient: " + bill.getPatient().getName());
            System.out.println("Amount: ₹" + bill.getPaidAmount());
            System.out.println("=====================================");
            
        } catch (Exception e) {
            logger.severe("Failed to send payment confirmation request email: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== PAYMENT CONFIRMATION REQUEST EMAIL (FALLBACK) ===");
            System.out.println("To: admin@hospital.com");
            System.out.println("Subject: Payment Confirmation Required - Bill #" + bill.getBillNumber());
            System.out.println("A new payment requires confirmation from patient: " + bill.getPatient().getName());
            System.out.println("Bill Number: " + bill.getBillNumber() + ", Amount: ₹" + bill.getPaidAmount());
            System.out.println("Error: " + e.getMessage());
            System.out.println("=====================================");
        }
    }
    
    
    public void sendPaymentConfirmedNotification(String to, String patientName, String billNumber, String amount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Payment Confirmed - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Great news! Your payment of ₹" + amount + " for Bill #" + billNumber + " has been confirmed.\n\n" +
                "Your payment is now complete and your bill has been marked as paid.\n\n" +
                "Payment Details:\n" +
                "- Bill Number: " + billNumber + "\n" +
                "- Amount Confirmed: ₹" + amount + "\n" +
                "- Confirmation Date: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                "Thank you for your payment. If you have any questions, please contact us.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Payment confirmed notification email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send payment confirmed notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== PAYMENT CONFIRMED NOTIFICATION EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Payment Confirmed");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your payment of ₹" + amount + " for Bill #" + billNumber + " has been confirmed.");
            System.out.println("Your payment is now complete.");
            System.out.println("=====================================");
        }
    }
    
    // Payment Rejected Notification
    public void sendPaymentRejectedNotification(String to, String patientName, String billNumber, String reason) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Payment Rejected - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "We regret to inform you that your payment for Bill #" + billNumber + " has been rejected.\n\n" +
                "Reason for rejection: " + (reason != null ? reason : "No specific reason provided") + "\n\n" +
                "Your bill status has been reset to pending. Please contact our billing department to resolve any issues.\n\n" +
                "You can make a new payment attempt once the issues are resolved.\n\n" +
                "If you believe this is an error, please contact us immediately.\n\n" +
                "Thank you for your understanding.\n\n" +
                "Best regards,\n" +
                "Hospital Management Team"
            );
            
            mailSender.send(message);
            logger.info("Payment rejected notification email sent successfully to: " + to);
            
        } catch (Exception e) {
            logger.severe("Failed to send payment rejected notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== PAYMENT REJECTED NOTIFICATION EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Payment Rejected");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your payment for Bill #" + billNumber + " has been rejected.");
            System.out.println("Reason: " + (reason != null ? reason : "No specific reason provided"));
            System.out.println("=====================================");
        }
    }

   
    public void sendBillOverdueNotification(String to, String patientName, String billNumber, String amount, String dueDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bill Overdue Reminder - Hospital Management System");
            message.setText(
                "Dear " + patientName + ",\n\n" +
                "Your bill is overdue. Please pay as soon as possible.\n\n" +
                "Bill Details:\n" +
                "- Bill Number: " + billNumber + "\n" +
                "- Amount Due: ₹" + amount + "\n" +
                "- Due Date: " + dueDate + "\n\n" +
                "If you have already paid, please ignore this message.\n\n" +
                "Thank you,\n" +
                "Hospital Management Team"
            );
            mailSender.send(message);
            logger.info("Bill overdue notification email sent successfully to: " + to);
        } catch (Exception e) {
            logger.severe("Failed to send bill overdue notification email to " + to + ": " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== BILL OVERDUE EMAIL ===");
            System.out.println("To: " + to);
            System.out.println("Subject: Bill Overdue Reminder");
            System.out.println("Dear " + patientName + ",");
            System.out.println("Your bill (Bill #" + billNumber + ") for ₹" + amount + " is overdue. Due date: " + dueDate);
            System.out.println("Please pay as soon as possible.");
            System.out.println("=====================================");
        }
    }
} 