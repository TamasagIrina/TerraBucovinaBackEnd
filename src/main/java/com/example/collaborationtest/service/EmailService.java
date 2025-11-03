package com.example.collaborationtest.service;

import com.example.collaborationtest.model.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender gmailSender;
    private final JavaMailSender yahooSender;

    private ProductService productService;

    private EmailProducts emailProducts;

    private ImageService imageService;

    @Autowired
    private TemplateEngine templateEngine;

    public EmailService(@Qualifier("gmailSender") JavaMailSender gmailSender,
                        @Qualifier("yahooSender") JavaMailSender yahooSender,
                        ProductService productService, ImageService imageService) {
        this.gmailSender = gmailSender;
        this.yahooSender = yahooSender;
        this.productService = productService;
        this.imageService = imageService;
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



    public void sendOrderConfirmationEmail(Order order) {
        List<EmailProducts> productDetails = new ArrayList<>();

        for (OrderProduct op : order.getProducts()) {
            Product product = productService.getProductById(op.getProduct().getId());
            Image image = imageService.findPrimaryByProduct_Id(op.getProduct().getId());

            EmailProducts dto = new EmailProducts(
                    product.getName(),
                    image != null ? "http://localhost:8080"+image.getImageUrl() : "",
                    op.getQuantity(),
                    product.getPrice()
            );

            productDetails.add(dto);
        }

        String domainPart = order.getEmail().split("@")[1];
        String provider = domainPart.split("\\.")[0];

        Context context = new Context();
        context.setVariable("fullName", order.getFullName());
        context.setVariable("products", productDetails);


        String htmlBody = templateEngine.process("orderConfirmation", context);
        sendEmail(order.getEmail(), "Confirmare comanda – Terra Bucovina", htmlBody, provider);
    }



}
