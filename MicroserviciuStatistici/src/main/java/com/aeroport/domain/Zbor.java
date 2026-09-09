package com.aeroport.domain;

import java.util.Map;

/**
 * Clasa Zbor reprezintă detaliile unui zbor preluat din microserviciul de zboruri.
 */
public class Zbor {

    // ID-ul unic al zborului (folosit pentru a căuta biletele asociate acestuia)
    private Integer id;

    // Numărul zborului (ex: "RO301"). Este folosit ca etichetă în grafice.
    private String numarZbor;

    // Numărul de locuri libere rămase în avion.
    private int locuriDisponibile;

    // Detaliile aeroportului de destinație. Folosim Map pentru a extrage cheia "oras" din răspunsul JSON.
    private Map<String, String> aeroportSosire;

    // -- Getteri și Setteri necesari pentru Jackson (biblioteca care transformă JSON în Obiecte) --

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumarZbor() { return numarZbor; }
    public void setNumarZbor(String numarZbor) { this.numarZbor = numarZbor; }

    public int getLocuriDisponibile() { return locuriDisponibile; }
    public void setLocuriDisponibile(int locuriDisponibile) { this.locuriDisponibile = locuriDisponibile; }

    public Map<String, String> getAeroportSosire() { return aeroportSosire; }
    public void setAeroportSosire(Map<String, String> aeroportSosire) { this.aeroportSosire = aeroportSosire; }
}