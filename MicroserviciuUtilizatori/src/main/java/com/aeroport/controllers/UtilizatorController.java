package com.aeroport.controllers;

import com.aeroport.domain.TipRol;
import com.aeroport.domain.Utilizator;
import com.aeroport.services.UtilizatoriService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Indică faptul că această clasă expune endpoint-uri REST (returnează date, de ex. JSON)
@RequestMapping("/api/utilizatori") // Rădăcina URL-ului pentru toate endpoint-urile din acest controller
public class UtilizatorController {

    private final UtilizatoriService utilizatoriService;

    public UtilizatorController(UtilizatoriService utilizatoriService) {
        this.utilizatoriService = utilizatoriService;
    }

    /**
     * Endpoint pentru Login: POST /api/utilizatori/login
     * Preia email-ul și parola din corpul cererii (JSON).
     */
    @PostMapping("/login")
    public ResponseEntity<Utilizator> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String parola = credentials.get("parola");

        Optional<Utilizator> utilizator = utilizatoriService.autentificare(email, parola);

        // Dacă autentificarea reușește, returnăm obiectul Utilizator (Cod 200 OK).
        // Dacă eșuează, returnăm doar codul de eroare HTTP 401 UNAUTHORIZED.
        return utilizator.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    /**
     * Endpoint pentru obținerea tuturor utilizatorilor: GET /api/utilizatori
     */
    @GetMapping
    public ResponseEntity<List<Utilizator>> getUtilizatori() {
        return ResponseEntity.ok(utilizatoriService.getUtilizatori());
    }

    /**
     * Endpoint pentru obținerea unui utilizator după ID: GET /api/utilizatori/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utilizator> getUtilizatorById(@PathVariable int id) {
        return utilizatoriService.getUtilizatorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Returnează 404 NOT FOUND dacă nu există
    }

    /**
     * Endpoint pentru filtrare după rol: GET /api/utilizatori/filtru?tip=ANGAJAT
     */
    @GetMapping("/filtru")
    public ResponseEntity<List<Utilizator>> getUtilizatoriByTip(@RequestParam TipRol tip) {
        return ResponseEntity.ok(utilizatoriService.getUtilizatoriFilterByTip(tip));
    }

    /**
     * Endpoint pentru crearea unui utilizator nou: POST /api/utilizatori
     */
    @PostMapping
    public ResponseEntity<String> insert(@RequestBody Utilizator utilizator) {
        try {
            if (utilizatoriService.insertUtilizator(utilizator)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Utilizator creat."); // HTTP 201
            }
            return ResponseEntity.internalServerError().build(); // HTTP 500
        } catch (DataIntegrityViolationException e) {
            // Prindem excepțiile aruncate de baza de date (ex: duplicate pe constrângeri UNIQUE)
            String message = e.getMostSpecificCause().getMessage().toLowerCase();

            // Verificăm dacă eroarea provine de la date duplicate (ex. MySQL Error 1062 sau SQLState 23000)
            if (message.contains("duplicate") || message.contains("23000")) {
                if (message.contains("telefon")) {
                    return ResponseEntity.badRequest().body("Eroare: Acest număr de telefon este deja asociat unui alt cont.");
                }
                if (message.contains("email")) {
                    return ResponseEntity.badRequest().body("Eroare: Această adresă de email este deja înregistrată.");
                }
            }

            return ResponseEntity.badRequest().body("Eroare de integritate: " + message);
        }
    }

    /**
     * Endpoint pentru actualizarea unui utilizator existent: PUT /api/utilizatori
     */
    @PutMapping
    public ResponseEntity<String> update(@RequestBody Utilizator utilizator) {
        try {
            if (utilizatoriService.updateUtilizator(utilizator)) {
                return ResponseEntity.ok("Utilizator actualizat.");
            }
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            // Aceeași logică de tratare a erorilor de duplicare ca la metoda de insert
            String message = e.getMostSpecificCause().getMessage().toLowerCase();

            if (message.contains("duplicate") || message.contains("23000")) {
                if (message.contains("telefon")) {
                    return ResponseEntity.badRequest().body("Eroare: Acest număr de telefon este deja asociat unui alt cont.");
                }
                if (message.contains("email")) {
                    return ResponseEntity.badRequest().body("Eroare: Această adresă de email este deja înregistrată.");
                }
            }

            return ResponseEntity.badRequest().body("Eroare de integritate: " + message);
        }
    }

    /**
     * Endpoint pentru ștergerea unui utilizator: DELETE /api/utilizatori/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (utilizatoriService.deleteUtilizator(id)) {
            return ResponseEntity.ok().build(); // HTTP 200 OK
        }
        return ResponseEntity.notFound().build(); // HTTP 404 NOT FOUND dacă ID-ul nu există
    }
}