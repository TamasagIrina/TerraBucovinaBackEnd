package com.example.collaborationtest.model;

import com.example.collaborationtest.enums.MessageStatus;
import com.example.collaborationtest.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contact_us_messsages")
public class ContactUsMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name = "name",nullable = false, length = 200)
    private String name;

    @Column(name = "email",nullable = false, length = 200)
    private String email;

    @Column(name = "phone_number",nullable = false, length = 50)
    private String phone_number;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "message", length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.ÎN_ASTEPTARE;

}
