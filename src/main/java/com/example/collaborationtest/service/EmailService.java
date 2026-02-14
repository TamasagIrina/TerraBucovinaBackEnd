package com.example.collaborationtest.service;

import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.*;
import com.example.collaborationtest.repository.UserRepo;
import com.resend.services.batch.model.CreateBatchEmailsResponse;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
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
import com.resend.*;

@Service
public class EmailService {

    private final JavaMailSender gmailSender;
    private final JavaMailSender yahooSender;

    private ProductService productService;


    private ImageService imageService;

    private UserRepo userRepo;

    @Value("${RESEND_FROM:onboarding@resend.dev}")
    private String from;

    @Autowired
    private TemplateEngine templateEngine;

    public EmailService(@Qualifier("gmailSender") JavaMailSender gmailSender,
                        @Qualifier("yahooSender") JavaMailSender yahooSender,
                        ProductService productService, ImageService imageService, UserRepo userRepo) {
        this.gmailSender = gmailSender;
        this.yahooSender = yahooSender;
        this.productService = productService;
        this.imageService = imageService;
        this.userRepo = userRepo;
    }



 
    @Async
    public void sendEmail(String to, String subject, String body, String provider, @Value("${RESEND_API_KEY}") String apiKey) {

        try {

            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(from)
                    .to(to)
                    .subject(subject)
                    .html(body)
                    .build();
            CreateEmailResponse data = resend.emails().send(params);
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR IN ASYNC EMAIL: " + e.getMessage());
            e.printStackTrace();
        }

    }



    @Async
    public void sendOrderConfirmationEmail(Order order) {
        List<EmailProducts> productDetails = new ArrayList<>();

        for (OrderProduct op : order.getProducts()) {
            Product product = productService.getProductById(op.getProduct().getId());
            Image image = imageService.findPrimaryByProduct_Id(op.getProduct().getId());

            EmailProducts dto = new EmailProducts(
                    product.getName(),
                    image != null ? "http://localhost:8080" + image.getImageUrl() : "",
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


        context.setVariable("address", order.getAddress());
        context.setVariable("city", order.getCity());
        context.setVariable("county", order.getCounty());
        context.setVariable("country", order.getCountry());

        String htmlBody = templateEngine.process("orderConfirmation", context);

        sendEmail(
                order.getEmail(),
                "Confirmare comanda - Terra Bucovina",
                htmlBody,
                provider
        );
    }


    @Async
    public void sendOrderStatusUpdateEmail(Order order) {
        List<EmailProducts> productDetails = new ArrayList<>();

        for (OrderProduct op : order.getProducts()) {
            Product product = productService.getProductById(op.getProduct().getId());
            Image image = imageService.findPrimaryByProduct_Id(op.getProduct().getId());

            EmailProducts dto = new EmailProducts(
                    product.getName(),
                    image != null ? "http://localhost:8080" + image.getImageUrl() : "",
                    op.getQuantity(),
                    product.getPrice()
            );

            productDetails.add(dto);
        }


        String domainPart = order.getEmail().split("@")[1];
        String provider = domainPart.split("\\.")[0];

        Context context = new Context();
        context.setVariable("fullName", order.getFullName());
        context.setVariable("orderId", order.getId());
        context.setVariable("status", order.getStatus());
        context.setVariable("products", productDetails);

        System.out.println("PRODUSE"+ productDetails.size());

        String htmlBody = templateEngine.process("orderStatusUpdate", context);

        sendEmail(
                order.getEmail(),
                "Status comanda – Terra Bucovina",
                htmlBody,
                provider
        );
    }


    @Async
    public void sendNewOrderNotificationToAdmins(Order order) {
        List<EmailProducts> productDetails = new ArrayList<>();

        for (OrderProduct op : order.getProducts()) {
            Product product = productService.getProductById(op.getProduct().getId());
            Image image = imageService.findPrimaryByProduct_Id(op.getProduct().getId());

            EmailProducts dto = new EmailProducts(
                    product.getName(),
                    image != null ? "http://localhost:8080" + image.getImageUrl() : "",
                    op.getQuantity(),
                    product.getPrice()
            );

            productDetails.add(dto);
        }


        List<User> adminUsers = userRepo.findAllByRoles(Role.ADMIN);



        for (User admin : adminUsers) {
            String email = admin.getEmail();
            String domainPart = email.split("@")[1];
            String provider = domainPart.split("\\.")[0];


            Context context = new Context();
            context.setVariable("fullName", order.getFullName());
            context.setVariable("email", order.getEmail());
            context.setVariable("phone", order.getPhone());

            context.setVariable("country", order.getCountry());
            context.setVariable("county", order.getCounty());
            context.setVariable("city", order.getCity());
            context.setVariable("postalCode", order.getPostalCode());
            context.setVariable("address", order.getAddress());

            context.setVariable("deliveryMethod", order.getDeliveryMethod());
            context.setVariable("paymentMethod", order.getPaymentMethod());

            context.setVariable("isCompanyInvoice", order.getIsCompanyInvoice());
            context.setVariable("cui", order.getCui());

            context.setVariable("products", productDetails);
            context.setVariable("orderId", order.getId());

            String htmlBody = templateEngine.process("adminOrderNotification", context);
            sendEmail(email, "Nouă comandă plasată – Terra Bucovina", htmlBody, provider);
        }
    }

    @Async
    public void sendContactResponseEmail(ContactUsMessages message) {
        String domainPart = message.getEmail().split("@")[1];
        String provider = domainPart.split("\\.")[0];

        Context context = new Context();
        context.setVariable("fullName", message.getName());
        context.setVariable("messageContent", message.getMessage());

        String htmlBody = templateEngine.process("addedMessageToContactUs", context);

        sendEmail(
                message.getEmail(),
                "Masajul– Terra Bucovina",
                htmlBody,
                provider
        );
    }

    @Async
    public void sendNewContactMessageToAdmins(ContactUsMessages message) {

        List<User> adminUsers = userRepo.findAllByRoles(Role.ADMIN);

        for (User admin : adminUsers) {
            String email = admin.getEmail();
            String domainPart = email.split("@")[1];
            String provider = domainPart.split("\\.")[0];

            Context context = new Context();
            context.setVariable("fullName", message.getName());
            context.setVariable("email", message.getEmail());
            context.setVariable("messageContent", message.getMessage());

            String htmlBody = templateEngine.process("adminNewMessage", context);

            sendEmail(
                    email,
                    "Mesaj nou primit - Terra Bucovina",
                    htmlBody,
                    provider
            );
        }
    }




}
