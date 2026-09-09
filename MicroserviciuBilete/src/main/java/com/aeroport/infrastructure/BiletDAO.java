package com.aeroport.infrastructure;

import com.aeroport.domain.Bilet;
import com.aeroport.domain.ZborID;
import com.aeroport.domain.dao.IBiletDAO;
import com.aeroport.infrastructure.tableEntities.BiletEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Component indică faptul că Spring va crea o singură instanță (Singleton) a acestei clase și o va gestiona.
 * Această clasă implementează IBiletDAO și acționează ca un intermediar între BiletRepository (DB) și Service.
 */
@Component
public class BiletDAO implements IBiletDAO {

    // Instanța repository-ului injectată de Spring
    private final BiletRepository biletRepository;

    @Autowired
    public BiletDAO(BiletRepository biletRepository) {
        this.biletRepository = biletRepository;
    }

    /**
     * Găsește toate biletele pentru un anumit zbor.
     */
    @Override
    public List<Bilet> getBiletePentruZbor(ZborID idZbor) {
        // Căutăm în BD entitățile, le trecem printr-un stream() și le transformăm (.map)
        // din BiletEntity în Bilet (Domain) folosind funcția toDomain() definită în entitate.
        return biletRepository.findByIdZbor(idZbor.getId())
                .stream()
                .map(BiletEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Găsește un bilet după ID-ul său unic.
     */
    @Override
    public Bilet getBiletById(int id) {
        // findById returnează un Optional. Dacă găsește biletul, îl mapează în Domain, altfel returnează null.
        return biletRepository.findById(id)
                .map(BiletEntity::toDomain)
                .orElse(null);
    }

    /**
     * Inserează un bilet nou în baza de date.
     */
    @Override
    public boolean insert(Bilet bilet) {
        try {
            // Convertim din Domain în Entity și salvăm.
            biletRepository.save(new BiletEntity(bilet));
            return true;
        } catch (Exception e) {
            // Dacă apare o eroare (ex: constrângeri de bază de date picate), prindem excepția.
            return false;
        }
    }

    /**
     * Șterge un bilet după ID.
     */
    @Override
    public boolean delete(int id) {
        // Verificăm întâi dacă biletul există, pentru a nu da eroare la ștergere.
        if (biletRepository.existsById(id)) {
            biletRepository.deleteById(id);
            return true;
        }
        return false; // Nu exista biletul cu acest ID
    }

    /**
     * Actualizează un bilet existent.
     */
    @Override
    public boolean update(Bilet bilet) {
        // 1. Verificare de siguranță: Ne asigurăm că am primit un ID valid
        if (bilet.getId() == null || bilet.getId().getId() == null) {
            System.err.println("❌ Eroare: Nu a fost furnizat niciun ID pentru update!");
            return false;
        }

        int idBilet = bilet.getId().getId();

        // 2. Căutăm entitatea existentă în baza de date folosind map() pe Optional
        return biletRepository.findById(idBilet).map(entitateExistenta -> {

            // 3. Modificăm doar câmpurile dorite pe entitatea deja scoasă din DB.
            entitateExistenta.setNumePasager(bilet.getNumePasager());
            entitateExistenta.setPretPlatit(java.math.BigDecimal.valueOf(bilet.getPretPlatit()));

            // 4. Salvăm. Pentru că 'entitateExistenta' are un ID activ, Spring/Hibernate va rula un query de UPDATE, nu INSERT.
            biletRepository.save(entitateExistenta);
            return true;

        }).orElseGet(() -> {
            // Executat dacă findById(idBilet) a returnat empty.
            System.err.println("❌ Eroare: Nu am putut găsi biletul cu ID-ul " + idBilet + " în baza de date.");
            return false;
        });
    }
}