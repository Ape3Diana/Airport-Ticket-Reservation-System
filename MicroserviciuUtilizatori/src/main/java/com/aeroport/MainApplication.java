package com.aeroport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        System.out.println("Swagger UI pentru Utilizatori: http://localhost:8081/swagger-ui/index.html");
    }
}