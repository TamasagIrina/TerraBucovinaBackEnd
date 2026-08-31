package com.example.collaborationtest.service;
import java.util.List;
public class BrevoSendEmailRequest {
    public Sender sender;
    public List<To> to;
    public String subject;
    public String htmlContent;

    public static class Sender {
        public String name;
        public String email;

        public Sender() {}
        public Sender(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    public static class To {
        public String email;
        public String name;

        public To() {}
        public To(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }
}
