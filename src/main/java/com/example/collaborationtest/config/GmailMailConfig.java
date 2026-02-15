package com.example.collaborationtest.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

//@Configuration
//public class GmailMailConfig {
//
//    @Value("${gmail.username}")
//    private String gmailUsername;
//
//    @Value("${gmail.password}")
//    private String gmailPassword;
//
//    @Bean("gmailSender")
//    public JavaMailSender getGmailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//        mailSender.setUsername(gmailUsername);
//        mailSender.setPassword(gmailPassword);
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.smtp.auth", true);
//        props.put("mail.smtp.starttls.enable", true);
//
//        return mailSender;
//    }
//}
