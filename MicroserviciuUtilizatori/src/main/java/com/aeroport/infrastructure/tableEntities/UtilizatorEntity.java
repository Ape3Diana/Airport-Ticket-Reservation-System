package com.aeroport.infrastructure.tableEntities;

import com.aeroport.domain.TipRol;
import com.aeroport.domain.Utilizator;
import com.aeroport.domain.UtilizatorID;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Această clasă reprezintă o tabelă din baza de date.
 * Fiecare instanță a clasei este un rând în tabela "utilizator".
 */
@Entity // Spune Spring-ului că aceasta este o entitate JPA
@Table(name = "utilizator") // Numele tabelei din baza de date
public class UtilizatorEntity {

    @Id // Marchează acest câmp ca fiind Cheia Primară (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID-ul este auto-incrementat de baza de date
    private Integer id;

    // unique = true previne înregistrarea mai multor conturi cu același email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String parola;

    // name = "nume_complet" face legătura cu coloana reală din baza de date
    @Column(name = "nume_complet", nullable = false, length = 100)
    private String numeComplet;

    @Enumerated(EnumType.STRING) // Salvează valoarea ENUM-ului ca text (ex: "ANGAJAT"), nu ca număr
    @Column(name = "tip_utilizator", nullable = false)
    private TipRol tipUtilizator;

    @Column(length = 20)
    private String telefon;

    // updatable = false împiedică modificarea datei de creare după inserare
    @Column(name = "data_creare", nullable = false, updatable = false, insertable = false)
    private LocalDateTime dataCreare;

    // Constructor gol necesar pentru JPA / Hibernate
    public UtilizatorEntity() {}

    /**
     * Mapare: Convertește un obiect din logica de business (Domain)
     * într-o entitate pregătită pentru a fi salvată în baza de date.
     */
    public UtilizatorEntity(Utilizator utilizator) {
        if (utilizator.getId() != null) {
            this.id = utilizator.getId().getId();
        }
        this.email = utilizator.getEmail();
        this.parola = utilizator.getParola();
        this.numeComplet = utilizator.getNumeComplet();
        this.tipUtilizator = utilizator.getTipUtilizator();
        this.telefon = utilizator.getTelefon();
        // dataCreare nu este setată manual la insert, este gestionată automat de baza de date (ex: default CURRENT_TIMESTAMP)
    }

    /**
     * Mapare: Convertește entitatea din baza de date
     * înapoi într-un obiect de business (Domain) pentru a fi folosit în aplicație.
     */
    public Utilizator toDomain() {
        return new Utilizator(
                new UtilizatorID(this.id),
                this.email,
                this.parola,
                this.numeComplet,
                this.tipUtilizator,
                this.telefon,
                this.dataCreare
        );
    }

    // Getteri și Setteri standard pentru manipularea câmpurilor
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }
    public String getNumeComplet() { return numeComplet; }
    public void setNumeComplet(String numeComplet) { this.numeComplet = numeComplet; }
    public TipRol getTipUtilizator() { return tipUtilizator; }
    public void setTipUtilizator(TipRol tipUtilizator) { this.tipUtilizator = tipUtilizator; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public LocalDateTime getDataCreare() { return dataCreare; }
    public void setDataCreare(LocalDateTime dataCreare) { this.dataCreare = dataCreare; }
}