package com.aeroport.domain.dao;

import com.aeroport.domain.Zbor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * În arhitectura hexagonală, aceste interfețe acționează drept Porturi. Stratul de domeniu
 * definește ce operațiuni are nevoie să execute pe date, urmând ca stratul de infrastructură
 * (clasele cu adnotarea @Component) să implementeze cum se face asta din punct de vedere tehnic
 * în baza de date SQL.
 */

public interface IZborDAO {
    // Extrage toate zborurile mapate direct la entități curate de domeniu
    List<Zbor> getZboruri();

    // Caută un zbor specific după ID-ul său unic
    Optional<Zbor> getZborById(int id);

    // Caută zboruri după o bucată din numărul lor (ex: "RO")
    List<Zbor> getZboruriByNumar(String numarZbor);

    // Căutarea avansată și filtrarea flexibilă după text (oraș/IATA) și un interval orar definit
    List<Zbor> getZboruriFiltrare(String plecare, String sosire, LocalDateTime start, LocalDateTime end);

    // Salvează starea unui zbor nou creat
    boolean insert(Zbor zbor);

    // Modifică detaliile sau numărul de locuri al unui zbor existent
    boolean update(Zbor zbor);

    // Șterge permanent un zbor după ID
    boolean delete(int id);
}