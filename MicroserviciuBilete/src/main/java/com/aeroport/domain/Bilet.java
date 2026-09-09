package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;

/**
 * Clasa Bilet este o "Entitate de Domain".
 * Aici stau regulile stricte de business (validările). Nu are adnotări de baze de date (@Entity).
 * Toate atributele sunt 'final' (Imutabilitate) - odată creat un bilet valid în memorie,
 * datele lui nu se pot corupe accidental pe parcurs.
 */
public class Bilet {
    private final BiletID id;
    private final ZborID idZbor;
    private final UtilizatorID idUtilizator;
    private final String numePasager;
    private final String numarLoc;
    private final double pretPlatit;
    private final LocalDateTime dataAchizitie;

    /**
     * Constructor cu validări integrate. Nu poți instanția un bilet invalid.
     * @JsonCreator permite transformarea automată din JSON-ul primit în Controller direct în acest obiect.
     */
    @JsonCreator
    public Bilet(BiletID id, ZborID idZbor, UtilizatorID idUtilizator, String numePasager,
                 String numarLoc, double pretPlatit, LocalDateTime dataAchizitie) {

        // ===============================
        // VALIDĂRI DE BUSINESS (Guard Clauses)
        // Ne asigurăm că obiectul creat are sens logic pentru aeroport.
        // ===============================
        if (numePasager == null || numePasager.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele pasagerului este obligatoriu.");
        }
        if (numarLoc == null || numarLoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Numărul locului nu poate fi gol.");
        }
        if (pretPlatit < 0) {
            throw new IllegalArgumentException("Prețul biletului nu poate fi negativ.");
        }
        if (idZbor == null || idUtilizator == null) {
            throw new IllegalArgumentException("Biletul trebuie să fie asociat unui zbor și unui utilizator.");
        }

        // Inițializarea atributelor
        this.id = id;
        this.idZbor = idZbor;
        this.idUtilizator = idUtilizator;
        this.numePasager = numePasager;
        this.numarLoc = numarLoc;
        this.pretPlatit = pretPlatit;
        // Dacă data nu este furnizată, setăm data curentă implicit
        this.dataAchizitie = (dataAchizitie != null) ? dataAchizitie : LocalDateTime.now();
    }

    // Getters pentru a putea citi datele în Service sau DAO
    public BiletID getId() { return id; }
    public ZborID getIdZbor() { return idZbor; }
    public UtilizatorID getIdUtilizator() { return idUtilizator; }
    public String getNumePasager() { return numePasager; }
    public String getNumarLoc() { return numarLoc; }
    public double getPretPlatit() { return pretPlatit; }
    public LocalDateTime getDataAchizitie() { return dataAchizitie; }
}