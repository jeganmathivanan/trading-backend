package com.jegan.service;

import com.jegan.Domain.VerificationType;
import com.jegan.model.User;
import com.jegan.model.VerificationCode;

public interface VerificationCodeService{

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;


    VerificationCode getVerificationCodeByUser(Long userId);


    void deleteVarificationCodeById(VerificationCode verificationCode);

}
