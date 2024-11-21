package com.jegan.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        // Subject and body content
        String subject = "Verify OTP";
        String text = "Your verification code is: " + otp;

        // Set up the email
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);  // If you want to send HTML content, set this to true
        mimeMessageHelper.setTo(email);

        // Set the sender's email (you can customize this with your application’s no-reply email)
        mimeMessageHelper.setFrom("no-reply@yourdomain.com");

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            // Log or throw custom exception
            throw new MailSendException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }
}
