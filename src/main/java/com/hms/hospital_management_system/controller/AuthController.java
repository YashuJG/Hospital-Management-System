package com.hms.hospital_management_system.controller;

import com.hms.hospital_management_system.dto.RegistrationRequest;
import com.hms.hospital_management_system.entity.PasswordResetToken;
import com.hms.hospital_management_system.repository.UserRepository;
import com.hms.hospital_management_system.service.PasswordResetService;
import com.hms.hospital_management_system.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, PasswordResetService passwordResetService, UserRepository userRepository) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "about"; 
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Please correct the errors below.");
            return "register";
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already registered. Please use a different email address.");
            return "register";
        }

        int age = Period.between(registrationRequest.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 1) {
            model.addAttribute("error", "Invalid date of birth. Patient must be at least 1 year old.");
            return "register";
        }

        try {
            userService.registerNewPatient(registrationRequest);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Registration failed", e);
            model.addAttribute("error", "An unexpected error occurred during registration. Please try again.");
            return "register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestPasswordReset(@RequestParam String email, RedirectAttributes redirectAttributes) {
        logger.info("Password reset request received for email: {}", email);
        try {
            boolean result = passwordResetService.requestPasswordReset(email);
            if (result) {
                redirectAttributes.addFlashAttribute("success", "A password reset link has been sent to your email.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Could not find any user with the email address.");
            }
        } catch (Exception e) {
            logger.error("Error processing password reset request", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred. Please try again later.");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        Optional<PasswordResetToken> resetTokenOpt = passwordResetService.getToken(token);
        
        if (resetTokenOpt.isPresent()) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired password reset token.");
            return "redirect:/login";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              RedirectAttributes redirectAttributes) {
        
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            boolean result = passwordResetService.resetPassword(token, password);
            if (result) {
                redirectAttributes.addFlashAttribute("success", "You have successfully changed your password.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid or expired password reset token.");
                return "redirect:/reset-password?token=" + token;
            }
        } catch (Exception e) {
            logger.error("Error resetting password", e);
            redirectAttributes.addFlashAttribute("error", "An error occurred while resetting your password.");
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
            .findFirst()
            .map(authority -> authority.getAuthority().replace("ROLE_", ""))
            .orElse("USER");

        model.addAttribute("username", authentication.getName());
        model.addAttribute("role", role);

        return "dashboard";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "home";
    }
}
    
   