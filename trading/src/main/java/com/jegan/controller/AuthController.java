package com.jegan.controller;

import com.jegan.config.JwtProvider;
import com.jegan.model.TwoFactorOTP;
import com.jegan.model.User;
import com.jegan.repository.UserRepository;
import com.jegan.response.AuthResponse;
import com.jegan.service.CustomeUserDetailsService;
import com.jegan.service.EmailService;
import com.jegan.service.TwoFactorOtpService;
import com.jegan.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private CustomeUserDetailsService customeUserDetailsService;

  @Autowired
  private TwoFactorOtpService twoFactorOtpService;

  @Autowired
  private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{

      User isEmailEXist = userRepository.findByEmail(user.getEmail());
       if(isEmailEXist != null){
           throw new Exception("email is already exist");
       }
        User newUser = new User();
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(newUser);


        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("register success");
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{


String userName=user.getEmail();
String password=user.getPassword();




        Authentication auth = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

       User authUser = userRepository.findByEmail(userName);
        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
          if(
                  oldTwoFactorOTP!=null
          ){
              twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
          }
          TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);


         emailService.sendVerificationOtpEmail(userName,otp);


        res.setSession((newTwoFactorOTP.getId()));
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("login success");
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails=customeUserDetailsService.loadUserByUsername(userName);

        if(userDetails ==null){
            throw new BadCredentialsException("invalid username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");

        }

        return new UsernamePasswordAuthenticationToken(userName,password,userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.VerifyTwoFactorOtp(twoFactorOTP,otp)){

            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentification verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }

        throw new Exception("invalid otp");
       // return null;
    }



}
