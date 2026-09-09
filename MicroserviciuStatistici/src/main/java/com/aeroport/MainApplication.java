package com.aeroport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {
        "com.aeroport.infrastructure.clients",
        "com.aeroport.infrastructure.clients",
        "com.aeroport"
})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        System.out.println("Swagger UI pentru Statistici: http://localhost:8084/swagger-ui/index.html");
    }
}