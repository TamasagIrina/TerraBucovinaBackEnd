package com.example.collaborationtest.service;

import com.example.collaborationtest.dto.product.ProductResponseDTO;
import com.example.collaborationtest.enums.Role;
import com.example.collaborationtest.model.ContactUsMessages;
import com.example.collaborationtest.model.Image;
import com.example.collaborationtest.model.Order;
import com.example.collaborationtest.model.OrderProduct;
import com.example.collaborationtest.model.Product;
import com.example.collaborationtest.model.User;
import com.example.collaborationtest.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link EmailService}. The core {@code sendEmail} is stubbed on a
 * spy so we can assert the orchestration (which template, subject, recipients)
 * without performing real HTTP calls to Brevo.
 */
class EmailServiceTest {

    private ProductService productService;
    private ImageService imageService;
    private UserRepo userRepo;
    private TemplateEngine templateEngine;
    private WebClient.Builder webClientBuilder;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        imageService = mock(ImageService.class);
        userRepo = mock(UserRepo.class);
        templateEngine = mock(TemplateEngine.class);
        webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.build()).thenReturn(mock(WebClient.class, RETURNS_DEEP_STUBS));

        emailService = new EmailService(productService, imageService, userRepo, webClientBuilder);
        ReflectionTestUtils.setField(emailService, "templateEngine", templateEngine);
        ReflectionTestUtils.setField(emailService, "apiUrl", "http://brevo.test/smtp/email");
        ReflectionTestUtils.setField(emailService, "apiKey", "test-key");
        ReflectionTestUtils.setField(emailService, "senderEmail", "sender@test.com");
        ReflectionTestUtils.setField(emailService, "senderName", "TerraBucovina");
    }

    private ProductResponseDTO sampleProduct() {
        return new ProductResponseDTO(1, "Miere de tei", new BigDecimal("25.00"),
                null, null, null, null, null, 10, true, null, null, null,
                1, "Miere", List.of(), List.of(), List.of());
    }

    private Order sampleOrder() {
        OrderProduct op = OrderProduct.builder()
                .product(Product.builder().id(1).build())
                .quantity(2)
                .build();
        return Order.builder()
                .id(100)
                .fullName("Ion Pop")
                .email("ion@example.com")
                .phone("0700000000")
                .totalPrice(new BigDecimal("50.00"))
                .products(List.of(op))
                .build();
    }

    @Test
    void sendOrderConfirmationEmail_usesConfirmationTemplateAndOrderRecipient() {
        EmailService svc = spy(emailService);
        doNothing().when(svc).sendEmail(anyString(), anyString(), anyString(), anyString());

        when(productService.getProductById(anyInt())).thenReturn(sampleProduct());
        when(imageService.findPrimaryByProduct_Id(anyInt())).thenReturn((Image) null);
        when(templateEngine.process(eq("orderConfirmation"), any(Context.class))).thenReturn("<html>ok</html>");

        svc.sendOrderConfirmationEmail(sampleOrder());

        verify(svc).sendEmail("ion@example.com", "Ion Pop",
                "Confirmare comanda - Terra Bucovina", "<html>ok</html>");
    }

    @Test
    void sendNewOrderNotificationToAdmins_emailsEveryAdmin() {
        EmailService svc = spy(emailService);
        doNothing().when(svc).sendEmail(anyString(), anyString(), anyString(), anyString());

        when(productService.getProductById(anyInt())).thenReturn(sampleProduct());
        when(imageService.findPrimaryByProduct_Id(anyInt())).thenReturn((Image) null);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("<html>admin</html>");

        User a1 = new User(); a1.setEmail("admin1@test.com"); a1.setUsername("admin1");
        User a2 = new User(); a2.setEmail("admin2@test.com"); a2.setUsername("admin2");
        when(userRepo.findAllByRoles(Role.ADMIN)).thenReturn(List.of(a1, a2));

        svc.sendNewOrderNotificationToAdmins(sampleOrder());

        verify(svc, times(2)).sendEmail(anyString(), anyString(),
                eq("Nouă comandă plasată – Terra Bucovina"), anyString());
        verify(svc).sendEmail(eq("admin1@test.com"), eq("admin1"), anyString(), anyString());
        verify(svc).sendEmail(eq("admin2@test.com"), eq("admin2"), anyString(), anyString());
    }

    @Test
    void sendContactResponseEmail_usesContactTemplateAndSenderIsTheMessageAuthor() {
        EmailService svc = spy(emailService);
        doNothing().when(svc).sendEmail(anyString(), anyString(), anyString(), anyString());

        when(templateEngine.process(eq("addedMessageToContactUs"), any(Context.class)))
                .thenReturn("<html>contact</html>");

        ContactUsMessages msg = ContactUsMessages.builder()
                .name("Maria")
                .email("maria@example.com")
                .message("Buna ziua")
                .build();

        svc.sendContactResponseEmail(msg);

        verify(svc).sendEmail(eq("maria@example.com"), eq("Maria"), anyString(), eq("<html>contact</html>"));
    }

    @Test
    void sendEmail_doesNotThrowWhenBrevoCallFails() {
        // WebClient is a deep-stub returning null on block(); the method must swallow errors.
        emailService.sendEmail("x@y.com", "X", "Subject", "<b>hi</b>");
    }
}
