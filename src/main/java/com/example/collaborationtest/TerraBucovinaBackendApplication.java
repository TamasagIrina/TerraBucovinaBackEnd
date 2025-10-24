package com.example.collaborationtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TerraBucovinaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerraBucovinaBackendApplication.class, args);
    }

}
