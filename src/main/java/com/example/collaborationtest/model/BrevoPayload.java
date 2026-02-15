package com.example.collaborationtest.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class BrevoPayload {
    private Sender sender;
    private List<Recipient> to;
    private String subject;
    private String htmlContent;

    @Data
    @Builder
    public static class Sender {
        private String name;
        private String email;
    }

    @Data
    @Builder
    public static class Recipient {
        private String name;
        private String email;
    }
}