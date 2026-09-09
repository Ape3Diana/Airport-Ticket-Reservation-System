package com.aeroport.domain.dao;

import com.aeroport.domain.Bilet;
import com.aeroport.domain.ZborID;
import java.util.List;

/**
 * Interfața reprezintă contractul pentru accesul la date.
 * Decuplează logica de business (Service) de implementarea exactă a bazei de date (BiletDAO).
 */
public interface IBiletDAO {
    List<Bilet> getBiletePentruZbor(ZborID idZbor);
    Bilet getBiletById(int id);
    boolean insert(Bilet bilet);
    boolean delete(int id);
    boolean update(Bilet bilet);
}