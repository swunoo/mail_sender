package com.rf.mail_sender.Service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(
        String to,
        String subject
    ) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Java HTML Mail");

        String htmlContent = "<html><body><h1>Hello World</h1>"
        + "<p>This email contains an inline image and a link:</p>"
        + "<img src='cid:image1' width='500' height='300'>"
        + "<a href='https://example.com'>This is a Link</a>"
        + "</body></html>";
        

        helper.setText(htmlContent, true);

        ClassPathResource imageResource = new ClassPathResource("images/img01.jpg");
        helper.addInline("image1", imageResource);

        mailSender.send(message);
    }
}

