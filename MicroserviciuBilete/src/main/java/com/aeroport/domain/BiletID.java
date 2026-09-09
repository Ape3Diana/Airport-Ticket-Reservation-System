package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Value Object pentru ID-ul Biletului.
 * Încapsulează un Integer pentru a oferi siguranță la tipizare în logica de business.
 */
public class BiletID {
    private final Integer id;

    /**
     * @JsonCreator îi spune lui Jackson (librăria care parsează JSON) cum să creeze acest obiect
     * atunci când primește un JSON cu un număr.
     */
    @JsonCreator
    public BiletID(Integer id) {
        this.id = id;
    }

    /**
     * @JsonValue îi spune lui Jackson că atunci când transformă acest obiect înapoi în JSON,
     * să scrie direct valoarea din interior (ex: 5), în loc de un obiect complex (ex: {"id": 5}).
     */
    @JsonValue
    public Integer getId() {
        return id;
    }
}