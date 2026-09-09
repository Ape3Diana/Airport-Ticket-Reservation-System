package com.aeroport.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Acestea sunt modelele tale de business. Spre deosebire de entitățile din infrastructură (ZborEntity),
 * acestea conțin logică și validări, auto-protejându-se împotriva stărilor invalide.
 */

public class Zbor {
    // --- Atribute ---
    private ZborID id;                     // ID-ul unic tipizat al zborului
    private String numarZbor;              // Codul zborului (ex: "RO201")
    private Aeroport aeroportPlecare;      // Obiectul complet al aeroportului de decolare
    private Aeroport aeroportSosire;       // Obiectul complet al aeroportului de destinație
    private LocalDateTime oraDecolare;     // Data și ora decolării
    private LocalDateTime oraAterizare;    // Data și ora aterizării
    private BigDecimal pretBilet;          // Prețul (folosim BigDecimal pentru precizie financiară)
    private int locuriDisponibile;         // Capacitatea curentă rămasă a avionului

    // --- Constructor cu Validări Automate ---
    public Zbor(ZborID id, String numarZbor, Aeroport aeroportPlecare, Aeroport aeroportSosire,
                LocalDateTime oraDecolare, LocalDateTime oraAterizare,
                BigDecimal pretBilet, int locuriDisponibile) {

        // 1. Validare: Un avion nu poate zbura spre același aeroport din care pleacă
        if (aeroportPlecare.getId().getId() == aeroportSosire.getId().getId()) {
            throw new IllegalArgumentException("Eroare: Aeroportul de plecare este identic cu cel de sosire.");
        }
        // 2. Validare: Regula companiei - capacitatea maximă permisă este de 90 de locuri
        if (locuriDisponibile > 90) {
            throw new IllegalArgumentException("Eroare: Capacitatea maximă a unui avion este de 90 de locuri!");
        }
        // 3. Validare: Nu putem avea un număr negativ de scaune în avion
        if (locuriDisponibile < 0) {
            throw new IllegalArgumentException("Eroare: Numărul de locuri nu poate fi negativ!");
        }
        // 4. Validare: Cronologia timpului - nu poți ateriza înainte să decolezi
        if (oraDecolare.isAfter(oraAterizare)) {
            throw new IllegalArgumentException("Eroare: Ora de decolare nu poate fi după ora de aterizare.");
        }

        // Atribuirea valorilor doar după ce toate testele de business au trecut cu succes
        this.id = id;
        this.numarZbor = numarZbor;
        this.aeroportPlecare = aeroportPlecare;
        this.aeroportSosire = aeroportSosire;
        this.oraDecolare = oraDecolare;
        this.oraAterizare = oraAterizare;
        this.pretBilet = pretBilet;
        this.locuriDisponibile = locuriDisponibile;
    }

    // --- Logica de Business Encapsulată (Comportamente) ---

    // Verifică dacă mai există cel puțin un scaun liber în avion
    public boolean areLocuriDisponibile() {
        return this.locuriDisponibile > 0;
    }

    // Scade un loc (apelată la cumpărarea unui bilet). Include barieră de protecție.
    public void scadeLocDisponibil() {
        if (areLocuriDisponibile()) {
            this.locuriDisponibile--;
        } else {
            throw new IllegalStateException("Nu mai sunt locuri disponibile pentru acest zbor.");
        }
    }

    // Suplimentează locurile (apelată când un bilet este anulat/șters de client)
    public void adaugaLocDisponibil() {
        this.locuriDisponibile++;
    }

    // --- Getteri ---
    public ZborID getId() { return id; }
    public String getNumarZbor() { return numarZbor; }
    public Aeroport getAeroportPlecare() { return aeroportPlecare; }
    public Aeroport getAeroportSosire() { return aeroportSosire; }
    public LocalDateTime getOraDecolare() { return oraDecolare; }
    public LocalDateTime getOraAterizare() { return oraAterizare; }
    public BigDecimal getPretBilet() { return pretBilet; }
    public int getLocuriDisponibile() { return locuriDisponibile; }
}