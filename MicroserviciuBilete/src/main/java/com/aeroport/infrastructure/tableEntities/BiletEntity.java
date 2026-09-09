package com.aeroport.infrastructure.tableEntities;

import com.aeroport.domain.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clasa BiletEntity reprezintă tabelul "bilet" din baza de date.
 * Folosim @Entity pentru a spune framework-ului Hibernate/JPA că aceasta este o entitate persistabilă.
 */
@Entity
@Table(name = "bilet") // Specifică numele exact al tabelului din baza de date
public class BiletEntity {

    // @Id marchează cheia primară a tabelului
    // @GeneratedValue(strategy = GenerationType.IDENTITY) înseamnă că ID-ul este auto-incrementat de baza de date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @Column mapează atributul la o coloană specifică. 'nullable = false' înseamnă că e obligatoriu (NOT NULL).
    @Column(name = "id_zbor", nullable = false)
    private int idZbor;

    @Column(name = "id_utilizator", nullable = false)
    private int idUtilizator;

    @Column(name = "nume_pasager", nullable = false)
    private String numePasager;

    @Column(name = "numar_loc", nullable = false)
    private String numarLoc;

    @Column(name = "pret_platit", nullable = false)
    private BigDecimal pretPlatit; // Folosim BigDecimal pentru precizie financiară

    @Column(name = "data_achizitie")
    private LocalDateTime dataAchizitie;

    /**
     * Constructor gol (Default).
     * Este OBLIGATORIU pentru ca JPA (Hibernate) să poată instanția obiectele când le citește din baza de date.
     */
    public BiletEntity() {}

    /**
     * Constructor care convertește un obiect de tip Domain (Bilet) într-o Entitate (BiletEntity).
     * Este folosit atunci când vrem să salvăm sau să dăm update unui bilet primit din Service.
     */
    public BiletEntity(Bilet bilet) {
        if (bilet.getId() != null && bilet.getId().getId() != null) {
            this.id = bilet.getId().getId(); // Setăm ID-ul doar dacă există (la UPDATE)
        }
        this.idZbor = bilet.getIdZbor().getId();
        this.idUtilizator = bilet.getIdUtilizator().getId();
        this.numePasager = bilet.getNumePasager();
        this.numarLoc = bilet.getNumarLoc();
        this.pretPlatit = BigDecimal.valueOf(bilet.getPretPlatit());
        this.dataAchizitie = bilet.getDataAchizitie();
    }

    /**
     * Metodă care convertește Entitatea (BiletEntity) înapoi într-un obiect de Domain (Bilet).
     * Este folosită când citim din baza de date și vrem să trimitem datele curate către Service/Controller.
     */
    public Bilet toDomain() {
        return new Bilet(
                new BiletID(this.id),
                new ZborID(this.idZbor),
                new UtilizatorID(this.idUtilizator),
                this.numePasager,
                this.numarLoc,
                this.pretPlatit.doubleValue(),
                this.dataAchizitie
        );
    }

    // ===============================
    // GETTERS ȘI SETTERS
    // Sunt necesari pentru ca JPA să poată accesa și modifica câmpurile private.
    // ===============================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdZbor() { return idZbor; }
    public void setIdZbor(int idZbor) { this.idZbor = idZbor; }

    public int getIdUtilizator() { return idUtilizator; }
    public void setIdUtilizator(int idUtilizator) { this.idUtilizator = idUtilizator; }

    public String getNumePasager() { return numePasager; }
    public void setNumePasager(String numePasager) { this.numePasager = numePasager; }

    public String getNumarLoc() { return numarLoc; }
    public void setNumarLoc(String numarLoc) { this.numarLoc = numarLoc; }

    public BigDecimal getPretPlatit() { return pretPlatit; }
    public void setPretPlatit(BigDecimal pretPlatit) { this.pretPlatit = pretPlatit; }

    public LocalDateTime getDataAchizitie() { return dataAchizitie; }
    public void setDataAchizitie(LocalDateTime dataAchizitie) { this.dataAchizitie = dataAchizitie; }
}