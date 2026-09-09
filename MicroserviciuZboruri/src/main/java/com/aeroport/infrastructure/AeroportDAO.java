package com.aeroport.infrastructure;

import com.aeroport.domain.Aeroport;
import com.aeroport.domain.dao.IAeroportDAO;
import com.aeroport.infrastructure.tableEntities.AeroportEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Aceste clase reprezintă Adaptoarele din arhitectura hexagonală. Rolul lor critic este de
 * a face conversia dintre modelele de baze de date (Entity) și modelele curate de business (Domain).
 */

@Component // Marcat drept componentă Spring pentru a fi detectat în Service
public class AeroportDAO implements IAeroportDAO {

    // --- Atribute ---
    // Repository-ul nativ de Spring Data JPA folosit pentru interacțiunea directă cu baza de date
    private final AeroportRepository aeroportRepository;

    // --- Constructor ---
    public AeroportDAO(AeroportRepository aeroportRepository) {
        this.aeroportRepository = aeroportRepository;
    }

    // --- Metode de Mapare și Persistență ---

    @Override
    public List<Aeroport> getAeroporturi() {
        // Citește entitățile din DB, le mapează la Domain prin AeroportEntity::toDomain și le pune în listă
        return aeroportRepository.findAll().stream()
                .map(AeroportEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Aeroport> getAeroportById(int id) {
        // Caută în DB și transformă rezultatul opțional în obiect de domeniu
        return aeroportRepository.findById(id)
                .map(AeroportEntity::toDomain);
    }

    @Override
    public boolean insert(Aeroport aeroport) {
        // Conversie inversă: Domain -> Entity pentru a fi salvat în DB
        AeroportEntity entity = new AeroportEntity(aeroport);
        aeroportRepository.save(entity);
        return true;
    }

    @Override
    public boolean update(Aeroport aeroport) {
        // Validare defensivă: Verifică dacă înregistrările există înainte de a face update
        if (aeroport.getId() == null || !aeroportRepository.existsById(aeroport.getId().getId())) {
            throw new IllegalArgumentException("Aeroportul cu ID-ul specificat nu exista.");
        }

        AeroportEntity entity = new AeroportEntity(aeroport);
        aeroportRepository.save(entity);
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!aeroportRepository.existsById(id)) {
            return false;
        }
        try {
            aeroportRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false; // În caz că există restricții de cheie străină (Foreign Key active în tabelul Zbor)
        }
    }
}