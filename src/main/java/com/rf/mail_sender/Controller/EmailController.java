package com.rf.mail_sender.Controller;

import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rf.mail_sender.Service.EmailService;

@RestController
public class EmailController {

    @Value("${target.recipient}")
    private String to;

    @Autowired
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {

        // String[] toMultiple = new String[]{"teaforrisefor001@gmail.com"};
        String subject = "Testing Spring Mail";
        
        try {
            // emailService.sendEmail(to, subject);
            emailService.sendEmail(to, subject);
            return "Email sent successfully!";

        } catch (MessagingException | IOException e) {
            System.out.println("===== Email Sending Error START =====");
            e.printStackTrace();
            System.out.println("===== Email Sending Error END =====");
            return "An error occured. Please see server logs.";
        }
        
    }
}