package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Value Object pentru ID-ul Utilizatorului.
 * Rolul său este să asigure siguranța la tipizare (Type Safety).
 * Astfel, compilatorul nu te va lăsa să trimiți din greșeală un ZborID acolo unde se cere un UtilizatorID.
 */
public class UtilizatorID {

    // Imutabilitate: Odată setat, ID-ul nu mai poate fi modificat.
    private final int id;

    /**
     * @JsonCreator îi spune lui Jackson (librăria de parsare JSON din Spring)
     * cum să creeze acest obiect atunci când primește un simplu număr (int) într-un request HTTP.
     */
    @JsonCreator
    public UtilizatorID(int id) {
        this.id = id;
    }

    /**
     * @JsonValue îi spune lui Jackson că, atunci când serializează acest obiect pentru a-l trimite
     * înapoi către client (ex: Postman/Angular/JavaFX), să returneze direct numărul (ex: 10),
     * în loc de o structură complexă de tipul {"id": 10}.
     */
    @JsonValue
    public int getId() {
        return id;
    }
}