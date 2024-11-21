package com.jegan.repository;

import com.jegan.model.ForgotpasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotpasswordToken, String> {
    ForgotpasswordToken findByUserId(Long userId);
}
