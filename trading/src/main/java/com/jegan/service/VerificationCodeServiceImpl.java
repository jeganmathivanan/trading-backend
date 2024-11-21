package com.jegan.service;

import com.jegan.Domain.VerificationType;
import com.jegan.model.User;
import com.jegan.model.VerificationCode;
import com.jegan.repository.VerificationCodeRepository;
import com.jegan.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {



    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCodenew = new VerificationCode();
        verificationCodenew.setOtp(OtpUtils.generateOTP());
        verificationCodenew.setVerificationType(verificationType);
        verificationCodenew.setUser(user);

        return verificationCodeRepository.save(verificationCodenew);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {

       Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(id);

       if(verificationCodeOptional.isPresent()){

           return verificationCodeOptional.get();
       }
        throw new Exception("verfication code not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVarificationCodeById(VerificationCode verificationCode) {
          verificationCodeRepository.delete(verificationCode);
    }
}
