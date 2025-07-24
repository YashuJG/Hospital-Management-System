package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.FeedbackRequest;
import com.hms.hospital_management_system.entity.Feedback;
import com.hms.hospital_management_system.service.FeedbackService;
import com.hms.hospital_management_system.repository.PatientRepository;
import com.hms.hospital_management_system.repository.DoctorRepository;
import com.hms.hospital_management_system.repository.AppointmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @GetMapping
    public String listFeedback(Model model) {
        List<Feedback> feedbacks = feedbackService.getApprovedFeedback();
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("overallRating", feedbackService.getOverallAverageRating());
        model.addAttribute("positiveCount", feedbackService.getPositiveFeedbackCount());
        model.addAttribute("negativeCount", feedbackService.getNegativeFeedbackCount());
        return "feedback/list";
    }
    
    @GetMapping("/create")
    public String showCreateFeedbackForm(Model model) {
        model.addAttribute("feedbackRequest", new FeedbackRequest());
        model.addAttribute("doctors", doctorRepository.findByActiveTrue());
        model.addAttribute("feedbackTypes", Feedback.FeedbackType.values());
        return "feedback/create";
    }
    
    @PostMapping("/create")
    public String createFeedback(@Valid @ModelAttribute FeedbackRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "feedback/create";
        }
        
        try {
          
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            Optional<com.hms.hospital_management_system.entity.Patient> patientOpt = patientRepository.findByEmail(userEmail);
            
            if (patientOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Patient not found. Please login with your patient account.");
                return "redirect:/feedback/create";
            }
            
            Feedback feedback = feedbackService.createFeedback(request, patientOpt.get().getId());
            redirectAttributes.addFlashAttribute("success", "Feedback submitted successfully! Thank you for your feedback.");
            return "redirect:/feedback";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit feedback: " + e.getMessage());
            return "redirect:/feedback/create";
        }
    }
    
    @GetMapping("/{id}")
    public String viewFeedback(@PathVariable Long id, Model model) {
        return feedbackService.getFeedbackById(id)
                .map(feedback -> {
                    model.addAttribute("feedback", feedback);
                    return "feedback/view";
                })
                .orElse("redirect:/feedback");
    }
    
  
    @GetMapping("/admin")
    public String adminFeedbackList(Model model) {
        List<Feedback> pendingFeedbacks = feedbackService.getPendingFeedback();
        List<Feedback> approvedFeedbacks = feedbackService.getApprovedFeedback();
        
        model.addAttribute("pendingFeedbacks", pendingFeedbacks);
        model.addAttribute("approvedFeedbacks", approvedFeedbacks);
        model.addAttribute("overallRating", feedbackService.getOverallAverageRating());
        model.addAttribute("positiveCount", feedbackService.getPositiveFeedbackCount());
        model.addAttribute("negativeCount", feedbackService.getNegativeFeedbackCount());
        
        return "feedback/admin-list";
    }
    
    @PostMapping("/{id}/approve")
    public String approveFeedback(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            feedbackService.approveFeedback(id);
            redirectAttributes.addFlashAttribute("success", "Feedback approved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to approve feedback: " + e.getMessage());
        }
        return "redirect:/feedback/admin";
    }
    
    @PostMapping("/{id}/reject")
    public String rejectFeedback(@PathVariable Long id,
                                @RequestParam String reason,
                                RedirectAttributes redirectAttributes) {
        try {
            feedbackService.rejectFeedback(id, reason);
            redirectAttributes.addFlashAttribute("success", "Feedback rejected successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reject feedback: " + e.getMessage());
        }
        return "redirect:/feedback/admin";
    }
    
    @PostMapping("/{id}/response")
    public String addAdminResponse(@PathVariable Long id,
                                  @RequestParam String response,
                                  RedirectAttributes redirectAttributes) {
        try {
            feedbackService.addAdminResponse(id, response);
            redirectAttributes.addFlashAttribute("success", "Response added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add response: " + e.getMessage());
        }
        return "redirect:/feedback/admin";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteFeedback(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            feedbackService.deleteFeedback(id);
            redirectAttributes.addFlashAttribute("success", "Feedback deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete feedback: " + e.getMessage());
        }
        return "redirect:/feedback/admin";
    }
    
 
    @GetMapping("/my-feedback")
    public String myFeedback(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<com.hms.hospital_management_system.entity.Patient> patientOpt = patientRepository.findByEmail(userEmail);
        
        if (patientOpt.isEmpty()) {
            return "redirect:/login?error=Patient not found";
        }
        
        List<Feedback> myFeedbacks = feedbackService.getFeedbackByPatient(patientOpt.get().getId());
        model.addAttribute("feedbacks", myFeedbacks);
        return "feedback/my-feedback";
    }
    
    
    @GetMapping("/doctor/{doctorId}")
    public String doctorFeedback(@PathVariable Long doctorId, Model model) {
        List<Feedback> doctorFeedbacks = feedbackService.getFeedbackByDoctor(doctorId);
        Double averageRating = feedbackService.getAverageRatingByDoctor(doctorId);
        
        model.addAttribute("feedbacks", doctorFeedbacks);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("doctorId", doctorId);
        return "feedback/doctor-feedback";
    }
    
 
    @GetMapping("/search")
    public String searchFeedback(@RequestParam String keyword, Model model) {
        List<Feedback> searchResults = feedbackService.searchFeedbackByKeyword(keyword);
        model.addAttribute("feedbacks", searchResults);
        model.addAttribute("searchKeyword", keyword);
        return "feedback/search-results";
    }
    
    
    @GetMapping("/statistics")
    public String feedbackStatistics(Model model) {
        Double overallRating = feedbackService.getOverallAverageRating();
        Long positiveCount = feedbackService.getPositiveFeedbackCount();
        Long negativeCount = feedbackService.getNegativeFeedbackCount();
        List<Feedback> recentFeedbacks = feedbackService.getFeedbackByDateRange(
            LocalDateTime.now().minusMonths(1), LocalDateTime.now());
        
        model.addAttribute("overallRating", overallRating);
        model.addAttribute("positiveCount", positiveCount);
        model.addAttribute("negativeCount", negativeCount);
        model.addAttribute("recentFeedbacks", recentFeedbacks);
        
        return "feedback/statistics";
    }
} 