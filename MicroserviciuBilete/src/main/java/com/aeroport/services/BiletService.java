package com.aeroport.services;

import com.aeroport.domain.Bilet;
import com.aeroport.domain.ZborID;
import com.aeroport.domain.dao.IBiletDAO;
import com.aeroport.domain.IZborClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BiletService orchestrează operațiunile. Aici unim Domain-ul cu infrastructura
 * (prin intermediul interfețelor, nu al implementărilor directe).
 */
@Service
public class BiletService {

    private final IBiletDAO biletDAO;
    private final IZborClient zborClient; // Depindem de interfața din Domain (Dependency Inversion)

    @Autowired
    public BiletService(IBiletDAO biletDAO, IZborClient zborClient) {
        this.biletDAO = biletDAO;
        this.zborClient = zborClient;
    }

    public List<Bilet> getBiletePentruZbor(int idZbor) {
        return biletDAO.getBiletePentruZbor(new ZborID(idZbor));
    }

    /**
     * @Transactional asigură că ambele operațiuni (rezervarea în microserviciul Zbor și salvarea în DB)
     * se fac cu succes, altfel se anulează amândouă.
     */
    @Transactional
    public boolean insertBilet(Bilet bilet) {
        // 1. Validare suplimentară: Obținem biletele curente pentru a verifica dublurile de locuri
        List<Bilet> bileteExistente = biletDAO.getBiletePentruZbor(bilet.getIdZbor());

        boolean loculEsteOcupat = bileteExistente.stream()
                .anyMatch(b -> b.getNumarLoc().equalsIgnoreCase(bilet.getNumarLoc()));

        if (loculEsteOcupat) {
            System.out.println("Eroare: Locul " + bilet.getNumarLoc() + " este deja ocupat!");
            return false;
        }

        // 2. Apelăm microserviciul Zboruri prin intermediul portului din Domain.
        // În spate, Spring va folosi ZborServiceAdapter pentru a executa apelul HTTP efectiv.
        zborClient.rezervaLoc(bilet.getIdZbor().getId());

        // 3. Salvăm biletul nou în baza noastră de date
        return biletDAO.insert(bilet);
    }

    public Bilet getBiletById(int id) {
        return biletDAO.getBiletById(id);
    }

    @Transactional
    public boolean deleteBilet(int id) {
        Bilet bilet = biletDAO.getBiletById(id);
        if (bilet == null) {
            return false;
        }

        // Anunțăm microserviciul Zboruri că am eliberat locul
        zborClient.elibereazaLoc(bilet.getIdZbor().getId());

        // Ștergem biletul din baza noastră de date
        return biletDAO.delete(id);
    }

    public boolean updateBilet(Bilet bilet) {
        return biletDAO.update(bilet);
    }
}