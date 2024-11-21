package com.jegan.service;

import com.jegan.model.TwoFactorAuth;
import com.jegan.model.TwoFactorOTP;
import com.jegan.model.User;

public interface TwoFactorOtpService {
    TwoFactorOTP createTwoFactorOtp(User user,String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean VerifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
