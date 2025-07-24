package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.entity.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class BillOverdueReminderScheduler {

    @Autowired
    private BillService billService;

    @Autowired
    private EmailService emailService;

   
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendOverdueBillReminders() {
        List<Bill> overdueBills = billService.getOverdueBills();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Bill bill : overdueBills) {
            String to = bill.getPatient().getEmail();
            String name = bill.getPatient().getName();
            String billNumber = bill.getBillNumber();
            String amount = bill.getBalanceAmount() != null ? bill.getBalanceAmount().toString() : "0";
            String dueDate = bill.getDueDate() != null ? bill.getDueDate().format(formatter) : "N/A";
            emailService.sendBillOverdueNotification(to, name, billNumber, amount, dueDate);
        }
    }
} 