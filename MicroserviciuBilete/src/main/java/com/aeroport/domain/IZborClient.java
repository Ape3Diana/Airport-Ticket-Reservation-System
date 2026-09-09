package com.aeroport.domain;

/**
 * Interfață din Domain care definește contractul (Port-ul) pentru comunicarea cu alte microservicii.
 * Domain-ul dictează CE are nevoie (să rezerve/elibereze un loc), dar nu îi pasă CUM se întâmplă
 * asta tehnic (HTTP, RabbitMQ, gRPC etc.).
 * Este complet decuplată de tehnologia HTTP sau framework-uri precum Feign.
 */
public interface IZborClient {
    void rezervaLoc(int idZbor);
    void elibereazaLoc(int idZbor);
}