package com.aeroport.infrastructure.clients;

import com.aeroport.domain.IZborClient; // Implementează portul din Domain
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adaptorul (Pattern-ul Adapter). Aceasta face puntea de legătură între cerința Domain-ului
 * (IZborClient) și implementarea tehnică de infrastructură (ZborFeignClient).
 * @Component permite Spring-ului să descopere această clasă și să o injecteze în BiletService.
 */
@Component
public class ZborServiceAdapter implements IZborClient {

    private final ZborFeignClient zborFeignClient;

    @Autowired
    public ZborServiceAdapter(ZborFeignClient zborFeignClient) {
        this.zborFeignClient = zborFeignClient;
    }

    @Override
    public void rezervaLoc(int idZbor) {
        // Traducem apelul "curat" din Domain în apelul HTTP murdar/tehnic Feign
        zborFeignClient.rezervaBiletInMicroserviciu(idZbor);
    }

    @Override
    public void elibereazaLoc(int idZbor) {
        // Traducem apelul din Domain în apelul HTTP Feign
        zborFeignClient.elibereazaLocInMicroserviciu(idZbor);
    }
}