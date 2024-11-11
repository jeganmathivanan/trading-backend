package com.jegan.model;

import com.jegan.Domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {

    private  boolean isEnabled = false;
    private VerificationType sendTo;

}
