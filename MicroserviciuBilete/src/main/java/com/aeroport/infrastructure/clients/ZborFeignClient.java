package com.aeroport.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Detaliu pur de infrastructură. Se ocupă strict de configurarea request-ului HTTP real.
 * Aici spunem Spring-ului unde să trimită datele și pe ce port.
 */
@FeignClient(name = "zbor-client", url = "http://localhost:8080/api/zboruri")
public interface ZborFeignClient {

    @PostMapping("/{id}/rezerva")
    void rezervaBiletInMicroserviciu(@PathVariable("id") int idZbor);

    @PostMapping("/{id}/elibereaza")
    void elibereazaLocInMicroserviciu(@PathVariable("id") int idZbor);
}