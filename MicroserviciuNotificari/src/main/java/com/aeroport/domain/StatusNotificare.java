package com.aeroport.domain;

/**
 * Enum care definește starea curentă a unei notificări.
 * Ajută la urmărirea ciclului de viață al mesajului (dacă a fost doar generat, expediat sau a eșuat).
 */
public enum StatusNotificare {
    IN_ASTEPTARE, // Notificarea a fost creată, dar utilizatorul nu a primit-o/citit-o încă
    TRIMIS,       // Notificarea a fost livrată cu succes
    ESUAT         // A apărut o eroare la trimitere
}