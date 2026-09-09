package com.aeroport.domain;

import java.time.LocalDateTime;

/**
 * Clasa centrală de business. Aici se află logica de validare a datelor
 * înainte ca ele să ajungă în baza de date.
 */
public class Utilizator {
    // Atributele care definesc un utilizator în logica aplicației
    private UtilizatorID id;
    private String email;
    private String parola;
    private String numeComplet;
    private TipRol tipUtilizator;
    private String telefon;
    private LocalDateTime dataCreare;

    /**
     * Constructorul de business. Este apelat la crearea unui utilizator nou
     * și conține toate regulile de validare.
     */
    public Utilizator(UtilizatorID id, String email, String parola, String numeComplet,
                      TipRol tipUtilizator, String telefon, LocalDateTime dataCreare) {

        // --- VALIDĂRI DE DOMENIU ---
        // Verificăm formatul de bază al email-ului
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Adresa de email este invalidă.");
        }

        // Dacă nu se furnizează o parolă, setăm una implicită ("123456")
        this.parola = (parola == null || parola.trim().isEmpty()) ? "123456" : parola;

        // Validăm prezența numărului de telefon
        if (telefon == null || telefon.trim().isEmpty()) {
            throw new IllegalArgumentException("Numărul de telefon este obligatoriu.");
        }

        // Validăm prezența numelui
        if (numeComplet == null || numeComplet.trim().isEmpty()) {
            throw new IllegalArgumentException("Numele complet este obligatoriu.");
        }

        // Rolul nu poate fi null
        if (tipUtilizator == null) {
            throw new IllegalArgumentException("Tipul utilizatorului trebuie specificat.");
        }

        // Atribuirea valorilor validate
        this.id = id;
        this.email = email;
        this.numeComplet = numeComplet;
        this.tipUtilizator = tipUtilizator;
        this.telefon = telefon;
        // Dacă data creării nu este trimisă, o inițializăm cu momentul curent
        this.dataCreare = (dataCreare != null) ? dataCreare : LocalDateTime.now();
    }

    /**
     * Constructor privat gol. Este necesar strict pentru librăriile de serializare/deserializare
     * (cum ar fi Jackson, care transformă JSON-ul în obiecte Java).
     */
    private Utilizator() {}

    // Getteri folosiți pentru a citi datele utilizatorului
    public UtilizatorID getId() { return id; }
    public String getEmail() { return email; }
    public String getParola() { return parola; }
    public String getNumeComplet() { return numeComplet; }
    public TipRol getTipUtilizator() { return tipUtilizator; }
    public String getTelefon() { return telefon; }
    public LocalDateTime getDataCreare() { return dataCreare; }
}