package com.aeroport.domain;

import java.time.LocalDateTime;

/**
 * Modelul principal de business (Domain Model) pentru o notificare.
 * Nu conține adnotări de bază de date (JPA), menținând logica curată.
 */
public class Notificare {
    private Integer id; // Identificatorul unic al notificării
    private int idUtilizator; // Identificatorul utilizatorului care primește notificarea (Value Object)
    private TipCanal tipCanal; // Canalul prin care se trimite (ex: SMS, EMAIL)
    private String subiect; // Titlul pe scurt al alertei
    private String mesaj; // Corpul principal al textului
    private StatusNotificare status; // Starea trimiterii (IN_ASTEPTARE, TRIMIS etc.)
    private LocalDateTime dataCreare; // Momentul la care a fost generată alerta

    /**
     * Constructor folosit pentru crearea sau reconstituirea unui obiect Notificare.
     * Conține logica de fallback: dacă statusul sau data nu sunt furnizate, se setează valori implicite.
     */
    public Notificare(Integer id, int idUtilizator, TipCanal tipCanal, String subiect,
                      String mesaj, StatusNotificare status, LocalDateTime dataCreare) {
        this.id = id;
        this.idUtilizator = idUtilizator;
        this.tipCanal = tipCanal;
        this.subiect = subiect;
        this.mesaj = mesaj;
        // Dacă nu se specifică un status la creare, pornește direct "IN_ASTEPTARE"
        this.status = status != null ? status : StatusNotificare.IN_ASTEPTARE;
        // Dacă nu se dă o dată, se consideră momentul curent
        this.dataCreare = dataCreare != null ? dataCreare : LocalDateTime.now();
    }

    // Getteri pentru citirea datelor
    public Integer getId() { return id; }
    public int getIdUtilizator() { return idUtilizator; }
    public TipCanal getTipCanal() { return tipCanal; }
    public String getSubiect() { return subiect; }
    public String getMesaj() { return mesaj; }
    public StatusNotificare getStatus() { return status; }
    public LocalDateTime getDataCreare() { return dataCreare; }

    // Setter doar pentru status, deoarece este singurul care se modifică în ciclul de viață
    public void setStatus(StatusNotificare status) { this.status = status; }
}