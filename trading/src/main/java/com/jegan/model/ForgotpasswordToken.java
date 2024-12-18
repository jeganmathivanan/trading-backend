package com.jegan.model;

import com.jegan.Domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotpasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private VerificationType verificationType;

    private  String sendTo;

}
