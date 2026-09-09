package com.aeroport.infrastructure.tableEntities;

import com.aeroport.domain.Zbor;
import com.aeroport.domain.ZborID;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Obiectele direct legate și mapate pe structura tabelelor
 * fizice SQL folosind adnotările Jakarta Persistence.
 */

@Entity
@Table(name = "zbor")
public class ZborEntity {

    // --- Coloane ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numar_zbor", nullable = false, length = 20)
    private String numarZbor;

    // Relație Many-To-One: Multe zboruri pot avea același aeroport ca punct de plecare.
    // EAGER forțează încărcarea automată a datelor aeroportului odată cu citirea zborului.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aeroport_plecare", nullable = false)
    private AeroportEntity aeroportPlecare;

    // Relație Many-To-One pentru destinație
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aeroport_sosire", nullable = false)
    private AeroportEntity aeroportSosire;

    @Column(name = "ora_decolare", nullable = false)
    private LocalDateTime oraDecolare;

    @Column(name = "ora_aterizare", nullable = false)
    private LocalDateTime oraAterizare;

    @Column(name = "pret_bilet", nullable = false, precision = 10, scale = 2)
    private BigDecimal pretBilet;

    @Column(name = "locuri_disponibile", nullable = false)
    private Integer locuriDisponibile;

    // --- Constructori ---
    public ZborEntity() {}

    // Mapping: Domain -> Entity
    public ZborEntity(Zbor zbor) {
        if (zbor.getId() != null && zbor.getId().getId() != 0) {
            this.id = zbor.getId().getId();
        } else {
            this.id = null; // Setat ca null explicit pentru ca auto-incrementarea DB să nu aibă conflicte
        }

        this.numarZbor = zbor.getNumarZbor();

        // Alocă entități proxy setându-le doar ID-urile pentru maparea corectă a cheilor străine
        this.aeroportPlecare = new AeroportEntity();
        this.aeroportPlecare.setId(zbor.getAeroportPlecare().getId().getId());

        this.aeroportSosire = new AeroportEntity();
        this.aeroportSosire.setId(zbor.getAeroportSosire().getId().getId());

        this.oraDecolare = zbor.getOraDecolare();
        this.oraAterizare = zbor.getOraAterizare();
        this.pretBilet = zbor.getPretBilet();
        this.locuriDisponibile = zbor.getLocuriDisponibile();
    }

    // Mapping: Entity -> Domain
    public Zbor toDomain() {
        return new Zbor(
                new ZborID(this.id),
                this.numarZbor,
                this.aeroportPlecare.toDomain(),
                this.aeroportSosire.toDomain(),
                this.oraDecolare,
                this.oraAterizare,
                this.pretBilet,
                this.locuriDisponibile
        );
    }

    // --- Getteri și Setteri ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNumarZbor() { return numarZbor; }
    public void setNumarZbor(String numarZbor) { this.numarZbor = numarZbor; }
    public AeroportEntity getAeroportPlecare() { return aeroportPlecare; }
    public void setAeroportPlecare(AeroportEntity aeroportPlecare) { this.aeroportPlecare = aeroportPlecare; }
    public AeroportEntity getAeroportSosire() { return aeroportSosire; }
    public void setAeroportSosire(AeroportEntity aeroportSosire) { this.aeroportSosire = aeroportSosire; }
    public LocalDateTime getOraDecolare() { return oraDecolare; }
    public void setOraDecolare(LocalDateTime oraDecolare) { this.oraDecolare = oraDecolare; }
    public LocalDateTime getOraAterizare() { return oraAterizare; }
    public void setOraAterizare(LocalDateTime oraAterizare) { this.oraAterizare = oraAterizare; }
    public BigDecimal getPretBilet() { return pretBilet; }
    public void setPretBilet(BigDecimal pretBilet) { this.pretBilet = pretBilet; }
    public Integer getLocuriDisponibile() { return locuriDisponibile; }
    public void setLocuriDisponibile(Integer locuriDisponibile) { this.locuriDisponibile = locuriDisponibile; }
}