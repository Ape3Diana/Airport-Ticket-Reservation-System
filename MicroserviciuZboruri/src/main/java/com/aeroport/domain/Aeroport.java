package com.aeroport.domain;

/**
 * Acestea sunt modelele tale de business. Spre deosebire de entitățile din infrastructură (ZborEntity),
 * acestea conțin logică și validări, auto-protejându-se împotriva stărilor invalide.
 */

public class Aeroport {
    // --- Atribute ---
    private AeroportID id;    // Identificatorul puternic tipizat al aeroportului
    private String nume;      // Numele oficial (ex: "Aeroportul Internațional Henri Coandă")
    private String oras;      // Orașul în care se află (folosit la filtrare)
    private String tara;      // Țara de proveniență
    private String codIata;   // Codul unic internațional din 3 litere (ex: OTP, CLJ)

    // --- Constructori ---
    // Constructor gol privat pentru instanțiere controlată sau reflexie internă
    private Aeroport() {}

    // Constructorul principal folosit pentru a construi un model complet validat de Aeroport
    public Aeroport(AeroportID id, String nume, String oras, String tara, String codIata) {
        this.id = id;
        this.nume = nume;
        this.oras = oras;
        this.tara = tara;
        this.codIata = codIata;
    }

    // --- Getteri ---
    // Clasici, doar pentru citirea datelor. Nu avem Setteri deoarece proprietățile unui aeroport nu ar trebui modificate direct fără control de business
    public AeroportID getId() { return id; }
    public String getNume() { return nume; }
    public String getOras() { return oras; }
    public String getTara() { return tara; }
    public String getCodIata() { return codIata; }
}