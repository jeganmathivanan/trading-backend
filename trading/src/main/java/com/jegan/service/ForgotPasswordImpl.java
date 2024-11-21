package com.jegan.service;

import com.jegan.Domain.VerificationType;
import com.jegan.model.ForgotpasswordToken;
import com.jegan.model.User;
import com.jegan.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordImpl implements  ForgotPasswordService {


    @Autowired
private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotpasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {
        ForgotpasswordToken token = new ForgotpasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);

        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotpasswordToken findById(String id) {
        Optional<ForgotpasswordToken> token = forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotpasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotpasswordToken token) {
           forgotPasswordRepository.delete(token);
    }
}
