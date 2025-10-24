package com.example.collaborationtest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender gmailSender;
    private final JavaMailSender yahooSender;

    public EmailService(@Qualifier("gmailSender") JavaMailSender gmailSender,
                        @Qualifier("yahooSender") JavaMailSender yahooSender) {
        this.gmailSender = gmailSender;
        this.yahooSender = yahooSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body, String provider) {
        JavaMailSender sender;

        // Alegerea providerului
        switch (provider.toLowerCase()) {
            case "gmail":
                sender = gmailSender;
                break;
            case "yahoo":
                sender = yahooSender;
                break;
            default:
                throw new IllegalArgumentException("Provider invalid: " + provider);
        }

        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(((JavaMailSenderImpl) sender).getUsername());

            sender.send(message);
            System.out.println("Email trimis cu succes prin " + provider);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
