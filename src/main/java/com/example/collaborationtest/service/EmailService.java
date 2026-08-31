package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.dashboard.MonthlySummaryResponseDTO;
import com.example.collaborationtest.dto.product.ProductResponseDTO;
import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.*;
import com.example.collaborationtest.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Sends transactional emails through the Brevo REST API (v3).
 * All public methods are async so the HTTP request is never blocked on mail
 * delivery; the order/admin variants are read-only transactional so lazy
 * associations can be initialized while building the email body.
 */
@Service
public class EmailService {

    private final ProductService productService;
    private final ImageService imageService;
    private final UserRepo userRepo;
    private final WebClient webClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${brevo.sender.email:teodor.binisor@gmail.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:TerraBucovina}")
    private String senderName;

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.api.url:https://api.brevo.com/v3/smtp/email}")
    private String apiUrl;

    public EmailService(ProductService productService, ImageService imageService, UserRepo userRepo,
                        WebClient.Builder webClientBuilder) {
        this.productService = productService;
        this.imageService = imageService;
        this.userRepo = userRepo;
        this.webClient = webClientBuilder.build();
    }

    /**
     * Core send: builds the Brevo payload and POSTs it. Failures are logged, not
     * rethrown, so a mail problem never breaks the caller's flow.
     */
    @Async
    public void sendEmail(String to, String toName, String subject, String body) {
        try {
            BrevoSendEmailRequest payload = new BrevoSendEmailRequest();
            payload.sender = new BrevoSendEmailRequest.Sender(senderName, senderEmail);
            payload.to = List.of(new BrevoSendEmailRequest.To(to, toName));
            payload.subject = subject;
            payload.htmlContent = body;

            this.webClient.post()
                    .uri(apiUrl)
                    .header("api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR IN ASYNC EMAIL: " + e.getMessage());
        }
    }

    private List<EmailProducts> buildProductDetails(Order order) {
        List<EmailProducts> productDetails = new ArrayList<>();
        for (OrderProduct op : order.getProducts()) {
            ProductResponseDTO product = productService.getProductById(op.getProduct().getId());
            Image image = imageService.findPrimaryByProduct_Id(op.getProduct().getId());

            productDetails.add(new EmailProducts(
                    product.name(),
                    image != null ? image.getImageUrl() : "",
                    op.getQuantity(),
                    product.price()
            ));
        }
        return productDetails;
    }

    @Async
    @Transactional(readOnly = true)
    public void sendOrderConfirmationEmail(Order order) {
        List<EmailProducts> productDetails = buildProductDetails(order);

        Context context = new Context();
        context.setVariable("fullName", order.getFullName());
        context.setVariable("products", productDetails);
        context.setVariable("totalPrice", order.getTotalPrice());
        context.setVariable("address", order.getAddress());
        context.setVariable("city", order.getCity());
        context.setVariable("county", order.getCounty());
        context.setVariable("country", order.getCountry());

        String htmlBody = templateEngine.process("orderConfirmation", context);

        sendEmail(order.getEmail(), order.getFullName(), "Confirmare comanda - Terra Bucovina", htmlBody);
    }

    @Async
    @Transactional(readOnly = true)
    public void sendOrderStatusUpdateEmail(Order order) {
        List<EmailProducts> productDetails = buildProductDetails(order);

        Context context = new Context();
        context.setVariable("fullName", order.getFullName());
        context.setVariable("orderId", order.getId());
        context.setVariable("status", order.getStatus());
        context.setVariable("totalPrice", order.getTotalPrice());
        context.setVariable("products", productDetails);

        String htmlBody = templateEngine.process("orderStatusUpdate", context);

        sendEmail(order.getEmail(), order.getFullName(), "Status comanda – Terra Bucovina", htmlBody);
    }

    @Async
    @Transactional(readOnly = true)
    public void sendNewOrderNotificationToAdmins(Order order) {
        List<EmailProducts> productDetails = buildProductDetails(order);
        List<User> adminUsers = userRepo.findAllByRoles(Role.ADMIN);

        for (User admin : adminUsers) {
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
            context.setVariable("totalPrice", order.getTotalPrice());
            context.setVariable("products", productDetails);
            context.setVariable("orderId", order.getId());

            String htmlBody = templateEngine.process("adminOrderNotification", context);
            sendEmail(admin.getEmail(), admin.getUsername(), "Nouă comandă plasată – Terra Bucovina", htmlBody);
        }
    }

    @Async
    public void sendMonthlySummaryToAdmins(MonthlySummaryResponseDTO summary) {
        List<User> adminUsers = userRepo.findAllByRoles(Role.ADMIN);

        for (User admin : adminUsers) {
            Context context = new Context();
            context.setVariable("adminName", admin.getUsername());
            context.setVariable("monthLabel", summary.monthLabel());
            context.setVariable("totalOrders", summary.totalOrders());
            context.setVariable("totalRevenue", summary.totalRevenue());
            context.setVariable("totalProducts", summary.totalProducts());
            context.setVariable("totalUsers", summary.totalUsers());
            context.setVariable("topProducts", summary.topProducts());

            String htmlBody = templateEngine.process("adminMonthlySummary", context);
            sendEmail(admin.getEmail(), admin.getUsername(),
                    "Rezumat lunar " + summary.monthLabel() + " – Terra Bucovina", htmlBody);
        }
    }

    @Async
    public void sendAccountConfirmationEmail(String to, String name, String confirmationUrl) {
        Context context = new Context();
        context.setVariable("fullName", name);
        context.setVariable("confirmationUrl", confirmationUrl);

        String htmlBody = templateEngine.process("accountConfirmation", context);

        sendEmail(to, name, "Confirmă adresa de email - Terra Bucovina", htmlBody);
    }

    @Async
    public void sendPasswordChangeConfirmationEmail(String to, String name, String confirmationUrl) {
        Context context = new Context();
        context.setVariable("fullName", name);
        context.setVariable("confirmationUrl", confirmationUrl);

        String htmlBody = templateEngine.process("passwordChangeConfirmation", context);

        sendEmail(to, name, "Confirmă schimbarea parolei - Terra Bucovina", htmlBody);
    }

    @Async
    public void sendContactResponseEmail(ContactUsMessages message) {
        Context context = new Context();
        context.setVariable("fullName", message.getName());
        context.setVariable("messageContent", message.getMessage());

        String htmlBody = templateEngine.process("addedMessageToContactUs", context);

        sendEmail(message.getEmail(), message.getName(), "Masajul– Terra Bucovina", htmlBody);
    }

    @Async
    public void sendNewContactMessageToAdmins(ContactUsMessages message) {
        List<User> adminUsers = userRepo.findAllByRoles(Role.ADMIN);

        for (User admin : adminUsers) {
            Context context = new Context();
            context.setVariable("fullName", message.getName());
            context.setVariable("email", message.getEmail());
            context.setVariable("messageContent", message.getMessage());

            String htmlBody = templateEngine.process("adminNewMessage", context);

            sendEmail(admin.getEmail(), admin.getUsername(), "Mesaj nou primit - Terra Bucovina", htmlBody);
        }
    }
}
