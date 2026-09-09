package com.aeroport.infrastructure.clients;

import com.aeroport.domain.Bilet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * Client Feign care comunică cu microserviciul "bilet-service".
 */
@FeignClient(name = "bilet-service", url = "http://localhost:8082/api/bilete")
public interface BiletClient {

    // Execută un GET request pentru a aduce biletele unui anumit zbor.
    // {idZbor} va fi înlocuit cu valoarea transmisă prin @PathVariable.
    @GetMapping("/zbor/{idZbor}")
    List<Bilet> getBiletePentruZbor(@PathVariable("idZbor") int idZbor);
}