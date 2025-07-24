package com.hms.hospital_management_system.service;

import com.hms.hospital_management_system.entity.PasswordResetToken;
import com.hms.hospital_management_system.entity.User;
import com.hms.hospital_management_system.repository.PasswordResetTokenRepository;
import com.hms.hospital_management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean requestPasswordReset(String email) {
        logger.info("Processing password reset request for email: {}", email);
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                logger.warn("User not found for email: {}", email);
                return false;
            }

            User user = userOptional.get();
            logger.info("User found: {} (ID: {})", user.getFullName(), user.getId());

            // Upsert Logic: Find existing token or create a new one
            PasswordResetToken token = tokenRepository.findByUserId(user.getId()).orElse(new PasswordResetToken());
            
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(LocalDateTime.now().plusHours(1)); // 1 hour expiry
            token.setUsed(false);

            tokenRepository.save(token);
            
            logger.info("Saved password reset token for user ID: {}", user.getId());

          
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), token.getToken());
            
            logger.info("Password reset email sent to {}", email);

            return true;
        } catch (DataIntegrityViolationException e) {
            logger.error("Error processing password reset request due to data integrity issue", e);
            throw new RuntimeException("Error processing password reset request: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during password reset for email {}: {}", email, e.getMessage(), e);
            return false;
        }
    }

    public Optional<PasswordResetToken> getToken(String token) {
        logger.info("Validating token: {}", token);
        try {
            Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
            if (tokenOpt.isPresent()) {
                PasswordResetToken resetToken = tokenOpt.get();
                boolean isValid = !resetToken.isExpired() && !resetToken.isUsed();
                logger.info("Token validation result: {} (Expired: {}, Used: {})", isValid, resetToken.isExpired(), resetToken.isUsed());
                return isValid ? Optional.of(resetToken) : Optional.empty();
            } else {
                logger.warn("Token not found: {}", token);
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error validating token", e);
            return Optional.empty();
        }
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        logger.info("Processing password reset for token: {}", token);
        try {
            Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
            if (tokenOpt.isPresent()) {
                PasswordResetToken resetToken = tokenOpt.get();
                if (!resetToken.isExpired() && !resetToken.isUsed()) {
                    User user = resetToken.getUser();
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);

                    resetToken.setUsed(true);
                    tokenRepository.save(resetToken);
                    
                    logger.info("Password reset successful for user: {} (ID: {})", user.getFullName(), user.getId());
                    return true;
                } else {
                    logger.warn("Token is expired or already used. Expired: {}, Used: {}", resetToken.isExpired(), resetToken.isUsed());
                    return false;
                }
            } else {
                logger.warn("Token not found for password reset: {}", token);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error resetting password", e);
            return false;
        }
    }

    @Transactional
    public void cleanupExpiredTokens() {
        try {
            logger.info("Starting cleanup of expired tokens");
            LocalDateTime now = LocalDateTime.now();
           
            java.util.List<PasswordResetToken> expiredTokens = tokenRepository.findAllByExpiryDateBefore(now);
            if (expiredTokens != null && !expiredTokens.isEmpty()) {
                tokenRepository.deleteAll(expiredTokens);
                logger.info("Cleanup completed. Deleted {} expired tokens", expiredTokens.size());
            } else {
                logger.info("No expired tokens to cleanup.");
            }
        } catch (Exception e) {
            logger.error("Error during token cleanup", e);
        }
    }
} 