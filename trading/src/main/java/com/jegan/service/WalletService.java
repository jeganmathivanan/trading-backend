package com.jegan.service;

import com.jegan.model.Order;
import com.jegan.model.User;
import com.jegan.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalance(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransaction(User sender, Wallet receiverWallet, Long money) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;

}
