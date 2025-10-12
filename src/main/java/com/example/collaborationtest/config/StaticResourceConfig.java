package com.example.collaborationtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.images.dir}")
    private String imagesDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** → file:/<imagesDir>/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imagesDir + "/")
                .setCachePeriod(31536000); // 1 an cache
    }
}
