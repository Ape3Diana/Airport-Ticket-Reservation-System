package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Aceste clase (AeroportID și ZborID) sunt Value Objects folosite pentru a asigura un tip puternic
 * (Strong Typing) pentru ID-uri. În loc să folosim un simplu int primitiv (care poate fi confundat ușor),
 * folosim obiecte dedicate.
 * Adnotările Jackson asigură că frontend-ul le poate citi și trimite ca pe niște numere simple în JSON.
 */

public class ZborID {
    // --- Atribute ---
    // Identificatorul unic imutabil al zborului
    private final int id;

    // --- Constructor ---
    @JsonCreator
    public ZborID(int id) {
        this.id = id;
    }

    // --- Metode ---
    @JsonValue
    public int getId() {
        return id;
    }
}