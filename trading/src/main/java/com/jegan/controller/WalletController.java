package com.jegan.controller;

import com.jegan.model.Order;
import com.jegan.model.User;
import com.jegan.model.Wallet;
import com.jegan.model.WalletTransaction;
import com.jegan.service.OrderService;
import com.jegan.service.UserService;
import com.jegan.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController  {

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderService orderService;


   @Autowired
    private UserService userService;


   @GetMapping("/api/wallet")
   public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet=walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED) ;
   }


   @PutMapping("/api/wallet/${walletId}/transfer")
   public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt, @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception{

      User senderUser = userService.findUserProfileByJwt(jwt);
      Wallet receiverWallet=walletService.findWalletById(walletId);
      Wallet wallet = walletService.walletToWalletTransaction(senderUser,receiverWallet, req.getAmount());
       return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
   }



    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId, @RequestBody WalletTransaction req) throws Exception{


       Order order = orderService.getOrderById(orderId);
       Wallet wallet = walletService.payOrderPayment(order,user);
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }
}
