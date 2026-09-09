package com.aeroport.domain.dao;

import com.aeroport.domain.TipRol;
import com.aeroport.domain.Utilizator;

import java.util.List;
import java.util.Optional;

/**
 * Contractul pentru accesarea datelor. Orice implementare va trebui
 * să ofere aceste metode pentru manipularea utilizatorilor.
 */
public interface IUtilizatorDAO {
    List<Utilizator> getUtilizatori();
    Optional<Utilizator> getUtilizatorById(int id);
    Optional<Utilizator> getUtilizatorByEmail(String email); // Extrem de utilă pentru Login
    List<Utilizator> getUtilizatoriFilterByTip(TipRol tip);

    boolean insert(Utilizator utilizator);
    boolean update(Utilizator utilizator);
    boolean delete(int id);
}