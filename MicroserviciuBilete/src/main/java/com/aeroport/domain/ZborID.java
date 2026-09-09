package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Value Object pentru ID-ul Zborului.
 * La fel ca UtilizatorID și BiletID, protejează logica de business împotriva erorilor umane
 * (ex: inversarea parametrilor într-o funcție).
 */
public class ZborID {

    // Atribut final pentru a garanta că starea obiectului nu se modifică după creare.
    private final int id;

    /**
     * @JsonCreator permite transformarea automată a unui 'int' primit dintr-un request JSON
     * într-o instanță validă de ZborID.
     */
    @JsonCreator
    public ZborID(int id) {
        this.id = id;
    }

    /**
     * @JsonValue extrage valoarea primitivă ('int'-ul) pentru a o scrie curat în răspunsurile JSON.
     */
    @JsonValue
    public int getId() {
        return id;
    }
}