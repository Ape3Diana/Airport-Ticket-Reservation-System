package com.aeroport.services;

import com.aeroport.domain.TipRol;
import com.aeroport.domain.Utilizator;
import com.aeroport.domain.dao.IUtilizatorDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marchează clasa ca fiind un serviciu de business administrat de Spring
public class UtilizatoriService {

    // Folosim interfața DAO pentru a rămâne decuplați de implementarea specifică a bazei de date
    private final IUtilizatorDAO utilizatorDAO;

    public UtilizatoriService(IUtilizatorDAO utilizatorDAO) {
        this.utilizatorDAO = utilizatorDAO;
    }

    /**
     * Logica de autentificare.
     * Caută utilizatorul după email. Dacă este găsit, verifică dacă parola corespunde.
     */
    public Optional<Utilizator> autentificare(String email, String parola) {
        Optional<Utilizator> userOpt = utilizatorDAO.getUtilizatorByEmail(email);

        // Dacă utilizatorul există (isPresent) și parola din baza de date este egală cu cea introdusă
        if (userOpt.isPresent() && userOpt.get().getParola().equals(parola)) {
            return userOpt;
        }
        return Optional.empty(); // Autentificare eșuată
    }

    public List<Utilizator> getUtilizatori() {
        return utilizatorDAO.getUtilizatori();
    }

    public Optional<Utilizator> getUtilizatorById(int id) {
        return utilizatorDAO.getUtilizatorById(id);
    }

    public List<Utilizator> getUtilizatoriFilterByTip(TipRol tip) {
        return utilizatorDAO.getUtilizatoriFilterByTip(tip);
    }

    public boolean insertUtilizator(Utilizator utilizator) {
        // Obiectul "utilizator" a fost deja validat în momentul instanțierii de către Controller
        // (în constructorul clasei Utilizator), deci îl putem trimite direct către DAO.
        return utilizatorDAO.insert(utilizator);
    }

    public boolean updateUtilizator(Utilizator utilizator) {
        return utilizatorDAO.update(utilizator);
    }

    public boolean deleteUtilizator(int id) {
        return utilizatorDAO.delete(id);
    }
}