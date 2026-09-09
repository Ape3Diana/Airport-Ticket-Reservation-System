package com.aeroport.domain;

/**
 * Value Object (Obiect de Valoare) care încapsulează ID-ul unui utilizator.
 * Adaugă un nivel de siguranță (type safety) pentru a nu confunda ID-ul cu alte numere întregi.
 */
public class UtilizatorID {
    private final int id;

    public UtilizatorID(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}