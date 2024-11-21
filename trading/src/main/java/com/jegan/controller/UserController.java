package com.jegan.controller;

import com.jegan.Domain.VerificationType;
import com.jegan.ForgotPasswordTokenRequest;
import com.jegan.model.ForgotpasswordToken;
import com.jegan.model.User;
import com.jegan.model.VerificationCode;
import com.jegan.request.ResetPasswordRequest;
import com.jegan.response.ApiResponse;
import com.jegan.response.AuthResponse;
import com.jegan.service.EmailService;
import com.jegan.service.ForgotPasswordService;
import com.jegan.service.UserService;
import com.jegan.service.VerificationCodeService;
import com.jegan.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController

public class UserController {
    @Autowired
private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    private String jwt;

    @GetMapping("api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @PostMapping("/api/users/verification/{verificationType}/{send-otp}")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws Exception {

//       this.jwt = jwt;
        User user=userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode != null){
verificationCode = verificationCodeService.sendVerificationCode(user,verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp() );
        }

        return new ResponseEntity<String>("Verification otp sent sucessfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());


String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL) ? verificationCode.getEmail():verificationCode.getMobile();
boolean isVerified = verificationCode.getOtp().equals(otp);

if(isVerified){
    User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),sendTo,user);

    verificationCodeService.deleteVarificationCodeById(verificationCode);

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
      }
        throw new Exception("wrong otp");
    }







    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestHeader("Authorization") String jwt,
                                                        @RequestBody ForgotPasswordTokenRequest req) throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotpasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token == null){
            token=forgotPasswordService.createToken(user,id,otp,req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("password reset otp sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/resetPassword/verify-otp")
    public ResponseEntity<ApiResponse> resetPasswordOtp(@RequestParam String id,@RequestHeader("Authorization") String jwt
    ,@RequestBody ResetPasswordRequest req) throws Exception {

        ForgotpasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());
    if(isVerified){
        userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
        ApiResponse res = new ApiResponse();
        res.setMessage("password update sucessfully");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }
      throw new Exception("wrong otp");
    }



}


