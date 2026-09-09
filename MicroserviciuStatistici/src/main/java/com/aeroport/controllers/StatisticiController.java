package com.aeroport.controllers;

import com.aeroport.domain.StatisticaGrafic;
import com.aeroport.services.StatisticiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Preia datele din Services și le expune.
 */
@RestController
@RequestMapping("/api/statistici")
public class StatisticiController {

    private final StatisticiService statisticiService;

    public StatisticiController(StatisticiService statisticiService) {
        this.statisticiService = statisticiService;
    }

    @GetMapping("/venituri")
    public ResponseEntity<List<StatisticaGrafic>> getVenituri() {
        return ResponseEntity.ok(statisticiService.getVenituriZboruri());
    }

    @GetMapping("/ocupare")
    public ResponseEntity<List<StatisticaGrafic>> getOcupare() {
        return ResponseEntity.ok(statisticiService.getGradOcupare());
    }

    @GetMapping("/destinatii")
    public ResponseEntity<List<StatisticaGrafic>> getTopDestinatii() {
        return ResponseEntity.ok(statisticiService.getBiletePeDestinatie());
    }
}