package com.aeroport.controllers;

import com.aeroport.domain.IExportStrategy;
import com.aeroport.services.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // Spune că această clasă tratează cereri HTTP și returnează date, nu pagini HTML
@RequestMapping("/api/export") // Baza rutei pentru tot controller-ul
public class ExportController {

    private final ExportService exportService;

    // Injectăm serviciul care conține logica de afaceri
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Metoda POST (pentru că trimitem o listă mare de date în corpul cererii).
     * @PathVariable extrage cuvântul de după "/api/export/" (ex: "pdf")
     * @RequestBody extrage JSON-ul trimis de front-end și îl mapează ca o Listă de Dictionare(Map)
     */
    @PostMapping("/{format}")
    public ResponseEntity<byte[]> export(
            @PathVariable("format") String format,
            @RequestBody List<Map<String, Object>> data) {

        try {
            // 1. Verificăm dacă formatul cerut de utilizator este valid
            IExportStrategy strategyInfo = exportService.getStrategy(format);
            if (strategyInfo == null) {
                return ResponseEntity.badRequest().body(("Format invalid: " + format).getBytes());
            }

            // 2. Delegăm generarea efectivă a fișierului către Serviciu
            byte[] fileBytes = exportService.generateExport(format, data);

            // 3. Setăm "HttpHeaders" pentru a instrui browserul.
            // CONTENT_DISPOSITION: "attachment" forțează browserul să descarce fișierul fizic,
            // în loc să încerce să-l deschidă/afișeze într-un tab nou.
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export." + strategyInfo.getFileExtension());

            // 4. Returnăm răspunsul HTTP 200 (OK) cu tot pachetul
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(strategyInfo.getContentType())) // MIME type
                    .body(fileBytes); // Fișierul binar

        } catch (IllegalArgumentException e) {
            // Eroare de logică (ex: format necunoscut)
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            // Erori neașteptate (ex: probleme la scrierea PDF-ului)
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // HTTP 500
        }
    }
}