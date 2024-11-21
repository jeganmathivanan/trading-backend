package com.jegan.service;

import com.jegan.Domain.VerificationType;
import com.jegan.model.ForgotpasswordToken;
import com.jegan.model.User;

public interface ForgotPasswordService {


    ForgotpasswordToken createToken(User user,
                                    String id,
                                    String otp, VerificationType verificationType, String sendTo);

    ForgotpasswordToken findById(String id);

    ForgotpasswordToken findByUser(Long userId);

    void deleteToken(ForgotpasswordToken token);
}
