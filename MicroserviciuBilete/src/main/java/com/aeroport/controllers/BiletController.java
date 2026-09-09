package com.aeroport.controllers;

import com.aeroport.domain.Bilet;
import com.aeroport.services.BiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController combină @Controller și @ResponseBody. Spune că returnează date (JSON), nu vizualizări (HTML).
 * @RequestMapping stabilește calea de bază pentru acest controller: "http://localhost:port/api/bilete"
 */
@RestController
@RequestMapping("/api/bilete")
public class BiletController {

    private final BiletService biletService;

    @Autowired
    public BiletController(BiletService biletService) {
        this.biletService = biletService;
    }

    /**
     * POST /api/bilete
     * Primește un JSON în @RequestBody și încearcă să creeze un bilet nou.
     * Returnează HTTP 201 (CREATED) la succes, sau 400 (BAD REQUEST) la eșec.
     */
    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody Bilet bilet) {
        boolean succes = biletService.insertBilet(bilet);
        if (succes) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    /**
     * GET /api/bilete/zbor/{idZbor}
     * Extrage "idZbor" din URL (@PathVariable) și returnează toate biletele pentru el.
     * Foarte util pentru a ști ce locuri desenăm ocupate în interfața de utilizator.
     */
    @GetMapping("/zbor/{idZbor}")
    public ResponseEntity<List<Bilet>> getBileteDupaZbor(@PathVariable int idZbor) {
        List<Bilet> bilete = biletService.getBiletePentruZbor(idZbor);
        return new ResponseEntity<>(bilete, HttpStatus.OK);
    }

    /**
     * GET /api/bilete/{id}
     * Caută și returnează un singur bilet după ID-ul lui.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bilet> getById(@PathVariable int id) {
        Bilet bilet = biletService.getBiletById(id);
        if (bilet != null) {
            return new ResponseEntity<>(bilet, HttpStatus.OK); // Returnează biletul găsit
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Biletul nu a fost găsit (404)
    }

    /**
     * PUT /api/bilete
     * Actualizează un bilet existent. ID-ul este de obicei inclus în JSON-ul din @RequestBody.
     */
    @PutMapping
    public ResponseEntity<Boolean> update(@RequestBody Bilet bilet) {
        boolean succes = biletService.updateBilet(bilet);
        return new ResponseEntity<>(succes, HttpStatus.OK);
    }

    /**
     * DELETE /api/bilete/{id}
     * Anulează/șterge biletul cu ID-ul respectiv.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable int id) {
        boolean succes = biletService.deleteBilet(id);
        if (succes) {
            return new ResponseEntity<>(true, HttpStatus.OK); // Șters cu succes
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND); // Nu a fost găsit pentru a fi șters
    }
}