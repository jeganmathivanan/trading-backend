package com.jegan.model;

import com.jegan.Domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    private Wallet wallet;

    private WalletTransactionType walletTransactionType;

   private LocalDate sate;
   private String transferId;
   private String purpose;
   private long amount;
}
