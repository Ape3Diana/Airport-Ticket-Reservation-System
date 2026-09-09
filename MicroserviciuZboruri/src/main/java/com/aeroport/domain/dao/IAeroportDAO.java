package com.aeroport.domain.dao;

import com.aeroport.domain.Aeroport;
import java.util.List;
import java.util.Optional;

/**
 * În arhitectura hexagonală, aceste interfețe acționează drept Porturi. Stratul de domeniu
 * definește ce operațiuni are nevoie să execute pe date, urmând ca stratul de infrastructură
 * (clasele cu adnotarea @Component) să implementeze cum se face asta din punct de vedere tehnic
 * în baza de date SQL.
 */

public interface IAeroportDAO {
    // Returnează o listă cu toate aeroporturile convertite în obiecte de domeniu
    List<Aeroport> getAeroporturi();

    // Caută un aeroport după ID și îl pune într-un container Optional securizat
    Optional<Aeroport> getAeroportById(int id);

    // Inserează un aeroport nou în sistem
    boolean insert(Aeroport aeroport);

    // Actualizează proprietățile unui aeroport existent
    boolean update(Aeroport aeroport);

    // Șterge un aeroport din baza de date pe baza ID-ului
    boolean delete(int id);
}