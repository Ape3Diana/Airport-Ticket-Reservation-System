package com.aeroport.domain.dao;

import com.aeroport.domain.Notificare;
import java.util.List;

/**
 * Contractul (Interfața) pentru lucrul cu datele.
 * Stratul Service va apela aceste metode fără să știe dacă în spate e MySQL, Postgres sau un fișier text.
 */
public interface INotificareDAO {
    // Inserează o notificare nouă în sistem
    void insert(Notificare n);

    // Extrage toate notificările care încă nu au fost trimise/citite de utilizator
    List<Notificare> getNotificariInAsteptare(int idUtilizator);

    // Schimbă statusul tuturor notificărilor în așteptare ale unui utilizator în "TRIMIS"
    void marcheazaCaTrimise(int idUtilizator);
}