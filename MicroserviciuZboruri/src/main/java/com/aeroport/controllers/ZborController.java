package com.aeroport.controllers;

import com.aeroport.domain.Aeroport;
import com.aeroport.domain.Zbor;
import com.aeroport.services.ZboruriService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Acest controller funcționează ca poartă de intrare (API) pentru aplicație.
 * El primește cererile HTTP de la frontend (JavaFX / Web), le validează formal și
 * pasează execuția către serviciu.
 */

@RestController
@RequestMapping("/api/zboruri")
public class ZborController {

    // --- Atribute ---
    // Serviciul injectat prin constructor care conține logica de business pentru zboruri.
    private final ZboruriService zboruriService;

    // --- Constructor ---
    // Spring injectează automat instanța de ZboruriService (Dependency Injection)
    public ZborController(ZboruriService zboruriService) {
        this.zboruriService = zboruriService;
    }

    // --- Metode (Endpoint-uri) ---

    // Aduce toate zborurile din sistem
    @GetMapping
    public ResponseEntity<List<Zbor>> getZboruri() {
        List<Zbor> zboruri = zboruriService.getZboruri();
        return ResponseEntity.ok(zboruri); // Returnează Status 200 OK și lista
    }

    // Caută un zbor specific după ID-ul său unic
    @GetMapping("/{id}")
    public ResponseEntity<Zbor> getZborById(@PathVariable("id") int id) {
        Optional<Zbor> zbor = zboruriService.getZborById(id);
        return zbor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // Status 404 dacă nu există
    }

    // Caută zboruri care conțin un anumit număr de zbor (ex: RO301)
    @GetMapping("/numar/{numarZbor}")
    public ResponseEntity<List<Zbor>> getZboruriByNumar(@PathVariable("numarZbor") String numarZbor) {
        List<Zbor> zboruri = zboruriService.getZboruriByNumar(numarZbor);
        return ResponseEntity.ok(zboruri);
    }

    // Filtrează avansat zborurile după oraș/IATA plecare, sosire și data decolare
    @GetMapping("/search")
    public ResponseEntity<List<Zbor>> getZboruriFiltrare(
            @RequestParam("from") String plecare,
            @RequestParam("to") String sosire,
            @RequestParam("date") String dataStr) {
        // Parsează string-ul primit (ex: "2026-05-17") într-un obiect LocalDate
        LocalDate dataSelectata = LocalDate.parse(dataStr);
        // Construiește intervalul de timp pentru întreaga zi (de la 00:00 la 23:59:59)
        LocalDateTime startOfDay = dataSelectata.atStartOfDay();
        LocalDateTime endOfDay = dataSelectata.atTime(23, 59, 59);

        List<Zbor> zboruri = zboruriService.getZboruriFiltrare(plecare, sosire, startOfDay, endOfDay);
        return ResponseEntity.ok(zboruri);
    }

    // Creează un zbor nou primind datele în format JSON
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Zbor zbor) {
        try {
            boolean success = zboruriService.insertZbor(zbor);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Zborul a fost creat cu succes.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la crearea zborului.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Prinde erorile de validare
        }
    }

    // Actualizează un zbor existent pe baza ID-ului din URL și a corpului JSON
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Zbor zbor) {
        try {
            boolean success = zboruriService.updateZbor(zbor);
            if (success) {
                return ResponseEntity.ok("Zborul a fost actualizat.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Șterge un zbor din sistem după ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        boolean success = zboruriService.deleteZbor(id);
        if (success) {
            return ResponseEntity.ok("Zborul a fost șters.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Logica bonus: Rezervă un loc la zborul trimis prin ID (scade numărul de locuri cu 1)
    @PostMapping("/{id}/rezerva")
    public ResponseEntity<String> rezervaBilet(@PathVariable("id") int id) {
        try {
            boolean success = zboruriService.rezervaBilet(id);
            if (success) {
                return ResponseEntity.ok("Bilet rezervat cu succes! S-a scazut un loc din zbor.");
            }
            return ResponseEntity.badRequest().body("Eroare la rezervare.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliberează un loc la un zbor (crește numărul de locuri cu 1, folosit când se șterge un bilet)
    @PostMapping("/{id}/elibereaza")
    public ResponseEntity<String> elibereazaLoc(@PathVariable("id") int id) {
        try {
            boolean success = zboruriService.elibereazaLoc(id);
            if (success) {
                return ResponseEntity.ok("Loc eliberat cu succes! S-a adăugat un loc la zbor.");
            }
            return ResponseEntity.badRequest().body("Eroare la eliberarea locului.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A apărut o eroare de server.");
        }
    }

    // Endpoint folosit pentru a încărca listele de aeroporturi din ComboBox-urile interfeței grafice
    @GetMapping("/aeroporturi")
    public ResponseEntity<List<Aeroport>> getAeroporturi() {
        List<Aeroport> aeroporturi = zboruriService.getAeroporturi();
        return ResponseEntity.ok(aeroporturi);
    }
}