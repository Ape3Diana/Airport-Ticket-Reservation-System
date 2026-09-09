package com.aeroport.domain.dao;

import com.aeroport.domain.Bilet;
import java.util.List;

/**
 * Contract din interiorul Domeniului.
 * Cere o modalitate de a obține biletele, fără a ști cum sunt aduse.
 */
public interface BiletProvider {
    List<Bilet> getBiletePentruZbor(int idZbor);
}