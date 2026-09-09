package com.aeroport.infrastructure.clients;

import com.aeroport.domain.Zbor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/**
 * Client Feign care comunică cu microserviciul "zbor-service".
 * url = locația microserviciului respectiv.
 */
@FeignClient(name = "zbor-service", url = "http://localhost:8080/api/zboruri")
public interface ZborClient {

    // Execută un GET request pe URL-ul de bază pentru a aduce toate zborurile.
    @GetMapping
    List<Zbor> getAllZboruri();
}