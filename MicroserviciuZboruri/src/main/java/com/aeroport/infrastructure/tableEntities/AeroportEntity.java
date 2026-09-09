package com.aeroport.infrastructure.tableEntities;

import com.aeroport.domain.Aeroport;
import com.aeroport.domain.AeroportID;
import jakarta.persistence.*;

/**
 * Obiectele direct legate și mapate pe structura tabelelor
 * fizice SQL folosind adnotările Jakarta Persistence.
 */

@Entity
@Table(name = "aeroport") // Mapare pe tabela fizică 'aeroport'
public class AeroportEntity {

    // --- Proprietăți / Coloane DB ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-Increment nativ în DB
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nume;

    @Column(nullable = false, length = 100)
    private String oras;

    @Column(nullable = false, length = 100)
    private String tara;

    @Column(name = "cod_iata", nullable = false, unique = true, length = 10)
    private String codIata;

    // --- Constructori ---

    // Obligatoriu pentru mecanismul intern Hibernate/JPA
    public AeroportEntity() {}

    // Cartografiază obiectul pur de Domeniu în obiect tehnic de tip Entitate (folosit la inserare/update)
    public AeroportEntity(Aeroport aeroport) {
        if (aeroport.getId() != null) {
            this.id = aeroport.getId().getId();
        }
        this.nume = aeroport.getNume();
        this.oras = aeroport.getOras();
        this.tara = aeroport.getTara();
        this.codIata = aeroport.getCodIata();
    }

    // --- Mecanism de conversie ---

    // Extrage datele tehnice și le asamblează în modelul de business pur
    public Aeroport toDomain() {
        return new Aeroport(
                new AeroportID(this.id),
                this.nume,
                this.oras,
                this.tara,
                this.codIata
        );
    }

    // --- Getteri și Setteri ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public String getOras() { return oras; }
    public void setOras(String oras) { this.oras = oras; }
    public String getTara() { return tara; }
    public void setTara(String tara) { this.tara = tara; }
    public String getCodIata() { return codIata; }
    public void setCodIata(String codIata) { this.codIata = codIata; }
}