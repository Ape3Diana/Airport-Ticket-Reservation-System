package com.aeroport.infrastructure.tableEntities;

import com.aeroport.domain.Notificare;
import com.aeroport.domain.StatusNotificare;
import com.aeroport.domain.TipCanal;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entitatea JPA care face legătura (mapping) între codul Java și tabela "notificare" din baza de date.
 */
@Entity
@Table(name = "notificare")
public class NotificareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Cheie primară auto-incrementată

    @Column(name = "id_utilizator", nullable = false)
    private int idUtilizator; // Foreign key logic către tabela de utilizatori

    @Enumerated(EnumType.STRING) // Salvăm în BD textul enum-ului (ex: "EMAIL"), nu indexul lui numeric
    @Column(name = "tip_notificare", nullable = false)
    private TipCanal tipNotificare;

    private String subiect;

    @Column(nullable = false)
    private String mesaj;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_trimitere")
    private StatusNotificare statusTrimitere;

    @Column(name = "data_creare")
    private LocalDateTime dataCreare;

    // Constructor gol necesar obligatoriu pentru framework-ul JPA
    public NotificareEntity() {}

    /**
     * Constructor pentru a transforma modelul de Domeniu în Entitate de bază de date (la salvare).
     */
    public NotificareEntity(Notificare n) {
        this.id = n.getId();
        this.idUtilizator = n.getIdUtilizator();
        this.tipNotificare = n.getTipCanal();
        this.subiect = n.getSubiect();
        this.mesaj = n.getMesaj();
        this.statusTrimitere = n.getStatus();
        this.dataCreare = n.getDataCreare();
    }

    /**
     * Transformă Entitatea din baza de date înapoi în obiectul curat de Domeniu (la citire).
     */
    public Notificare toDomain() {
        return new Notificare(id, idUtilizator, tipNotificare, subiect, mesaj, statusTrimitere, dataCreare);
    }

    // Setteri și getteri specifici JPA
    public void setStatusTrimitere(StatusNotificare status) { this.statusTrimitere = status; }
}