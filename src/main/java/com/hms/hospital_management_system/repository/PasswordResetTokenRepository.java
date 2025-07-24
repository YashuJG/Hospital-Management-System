package com.hms.hospital_management_system.repository;

import com.hms.hospital_management_system.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByUserId(Long userId);

    List<PasswordResetToken> findAllByExpiryDateBefore(LocalDateTime expiryDate);

    void deleteByUser_Id(Long userId);
} 