package com.jegan.service;

import com.jegan.Domain.OrderType;
import com.jegan.model.Order;
import com.jegan.model.User;
import com.jegan.model.Wallet;
import com.jegan.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WalletServiceImpl implements WalletService {


    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {

        Wallet wallet = walletRepository.findByUserId(user.getId());

        if(wallet == null){
            wallet= new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {

        BigDecimal balance=wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {

        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()){
          return wallet.get();
        }
        throw new Exception("wallet not found");
    }

    @Override
    public Wallet walletToWalletTransaction(User sender, Wallet receiverWallet, Long amount) throws Exception {

       Wallet senderWallet=getUserWallet(sender);
       if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
           throw new Exception("insufficient balance");
       }
       BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
       senderWallet.setBalance(senderBalance);
       walletRepository.save(senderWallet);
       BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if(order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance= wallet.getBalance().subtract(order.getPrice());
           if(newBalance.compareTo(order.getPrice())<0){
               throw new Exception(("Insufficient funds for this transaction"));
           }
           wallet.setBalance(newBalance);

        }
        else {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);
        return wallet;
    }
}
