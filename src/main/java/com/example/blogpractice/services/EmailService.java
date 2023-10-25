package com.example.blogpractice.services;

public interface EmailService {

    void sendSimpleMailMessage(String name, String to, String token);
    void sendMimeMessageWithAttachments(String name, String to, String token);
    void sendMimeMessageWithEmbedded(String name, String to, String token);
    void sendHtmlMailWithEmbeddedFiles(String name, String to, String token);
    void sendHtmlMail(String name, String to, String token);
}
