package com.aeroport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Aceste clase (AeroportID și ZborID) sunt Value Objects folosite pentru a asigura un tip puternic
 * (Strong Typing) pentru ID-uri. În loc să folosim un simplu int primitiv (care poate fi confundat ușor),
 * folosim obiecte dedicate.
 * Adnotările Jackson asigură că frontend-ul le poate citi și trimite ca pe niște numere simple în JSON.
 */

public class AeroportID {
    // --- Atribute ---
    // ID-ul numeric intern, marcat ca final deoarece un Value Object este imutabil (nu își schimbă starea)
    private final int id;

    // --- Constructor ---
    // @JsonCreator îi spune librăriei Jackson că poate crea acest obiect direct dintr-un simplu număr primit în JSON
    @JsonCreator
    public AeroportID(int id) {
        this.id = id;
    }

    // --- Metode ---
    // @JsonValue serializează acest obiect în JSON direct ca pe un număr (ex: 5 în loc de {"id": 5})
    @JsonValue
    public int getId() {
        return id;
    }
}