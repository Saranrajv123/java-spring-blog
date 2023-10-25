package com.example.blogpractice.services.impl;

import com.example.blogpractice.services.EmailService;
import com.example.blogpractice.utils.EmailUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${}")
    private String emailHost;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("User Account verification");
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(EmailUtils.getMessage(name, emailHost, token));
            mailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());

        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setPriority(1);
            messageHelper.setSubject("User Account verification");
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setText(EmailUtils.getMessage(name, emailHost, token));

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }


    }

    @Override
    public void sendMimeMessageWithEmbedded(String name, String to, String token) {

    }

    @Override
    public void sendHtmlMailWithEmbeddedFiles(String name, String to, String token) {

    }

    @Override
    public void sendHtmlMail(String name, String to, String token) {

    }
}
