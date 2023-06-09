package com.rf.mail_sender.Service;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

public class SimpleEmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public SimpleEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String htmlBody) throws MessagingException {
        // SimpleMailMessage message = new SimpleMailMessage();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        /* Add Attachment
        File attachment = new File(attachmentPath);
        helper.addAttachment(attachment.getName(), attachment);
         */
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        
        mailSender.send(message);
    }

    public void sendLessSimpleEmail(
        String[] to,
        String subject
    ) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        String htmlBody = "<h1>HTML EMAIL SENDING</h1>" +
        "<p>Hello, <strong>Spring Mail</strong>!</p><a href=\"https://example.com\">An Anchor Tag</a>" +
        "<img src=\"https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000\">";

        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    public void sendEmail_Draft(
        String[] to,
        String subject
    ) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        // Content
        String simpleContent = "<h1>This is an HTML email</h1>";

        // String htmlBody = "<h1>This is an HTML email</h1>" +
        // "<p>Hello, <strong>Spring Mail</strong>!</p>" +
        // "<img src=\"cid:image001\">" +
        // "<video width=\"320\" height=\"240\" controls>" +
        // "  <source src=\"cid:video001\" type=\"video/mp4\">" +
        // "</video>";

        String htmlBody = "<h1>HTML EMAIL SENDING</h1>" +
        "<p>Hello, <strong>Spring Mail</strong>!</p><a href=\"https://example.com\">An Anchor Tag</a>" +
        "<img src=\"https://img.freepik.com/free-photo/wide-angle-shot-single-tree-growing-clouded-sky-during-sunset-surrounded-by-grass_181624-22807.jpg?w=2000\">" + "<video width=\"640\" height=\"360\" controls>" + 
        "<source src=\"https://youtu.be/Yjrfm_oRO0w\" type=\"video/mp4\">"+"</video>";

        // Comment: Images work, but not yet videos.

        // Add inline images
        // ClassPathResource imageResource = new ClassPathResource("/images/img01.avif");
        String DIR = System.getProperty("user.dir");
        File file = new File(DIR + "/src/main/resources/images/img01.avif");
        System.out.println();

        if(!file.exists()){
            try {
                System.out.println("========================");
                System.out.println("========================");
                throw new Exception("NO FILE EXISTS.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        FileSystemResource imageResource = new FileSystemResource(file);
        // helper.addInline(file.getName(), imageResource);
        
        // // Add inline videos (thumbnails)
        // ClassPathResource videoResource = new ClassPathResource("videos/vid01.mp4");
        // helper.addInline("video01", videoResource.getFile());

        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
    public void sendEmail_Working(
        String to,
        String subject
    ) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multipart = new MimeMultipart("related");

        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>Hello</H1><img src=\"cid:image\">";
        messageBodyPart.setContent(htmlText, "text/html");
        // add it
        multipart.addBodyPart(messageBodyPart);

        // second part (the image)
        DataSource fds = new FileDataSource(
           System.getProperty("user.dir") + "/src/main/resources/images/img01.jpg");

        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");

        // add image to the multipart
        multipart.addBodyPart(messageBodyPart);

        // put everything together
        message.setContent(multipart);

        mailSender.send(message);
    }
    public void sendEmail(
        String to,
        String subject
    ) throws MessagingException, IOException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("HTML email with inline images");

        String htmlContent = "<html><body><h1>Inline Images and Video</h1>"
        + "<p>This email contains inline images and a video:</p>"
        + "<img src='cid:image1' width='500' height='300'>"
        + "<br>"
        + "<video width='500' height='300' controls>"
        + "<source src='cid:video1' type='video/mp4'>"
        + "Your browser does not support the video tag."
        + "</video>"
        + "</body></html>";
        

        helper.setText(htmlContent, true);

        ClassPathResource imageResource = new ClassPathResource("images/img01.jpg");
        helper.addInline("image1", imageResource);

        ClassPathResource videoResource = new ClassPathResource("videos/vid01.mp4");
        helper.addInline("video1", videoResource, "video/mp4");

        mailSender.send(message);
    }
}

