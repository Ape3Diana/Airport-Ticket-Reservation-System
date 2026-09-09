package com.aeroport.controllers;

import com.aeroport.domain.Notificare;
import com.aeroport.services.NotificariService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller-ul REST care expune capabilitățile aplicației tale în afara rețelei.
 */
@RestController
@RequestMapping("/api/notificari")
public class NotificareController {

    // Injectăm serviciul care știe să proceseze cererile
    private final NotificariService service;

    public NotificareController(NotificariService service) {
        this.service = service;
    }

    /**
     * Endpoint folosit de Microserviciul Utilizatori (via Feign Client).
     * Apelat automat când un utilizator face o schimbare critică la contul său.
     * * @param payload Datele primite sub formă de JSON, convertite în Map (conțin idUtilizator și mesaj)
     */
    @PostMapping("/trimite")
    public void trimiteAlertaSecuritate(@RequestBody Map<String, Object> payload) {
        // Extragem datele din cererea HTTP
        int idUtilizator = (Integer) payload.get("idUtilizator");
        String mesaj = (String) payload.get("mesaj");

        // Delegăm procesarea către stratul de serviciu
        service.notificaModificareCont(idUtilizator, mesaj);
    }

    /**
     * Endpoint apelat de aplicația de interfață (ex: Clientul JavaFX Desktop)
     * în momentul în care userul face login cu succes, pentru a arăta pop-up-uri cu alertele recente.
     * * @param idUtilizator ID-ul luat din URL (path variable)
     * @return O listă JSON cu notificările aflate în așteptare pentru acest utilizator
     */
    @GetMapping("/asteptare/{idUtilizator}")
    public List<Notificare> popupsLaLogin(@PathVariable int idUtilizator) {
        return service.getSipreiaNotificariAsteptare(idUtilizator);
    }
}