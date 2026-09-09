package com.aeroport.infrastructure;

import com.aeroport.domain.Zbor;
import com.aeroport.domain.dao.IZborDAO;
import com.aeroport.infrastructure.tableEntities.ZborEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Aceste clase reprezintă Adaptoarele din arhitectura hexagonală. Rolul lor critic este de a face
 * conversia dintre modelele de baze de date (Entity) și modelele curate de business (Domain).
 */

@Component
public class ZborDAO implements IZborDAO {

    // --- Atribute ---
    private final ZborRepository zborRepository;

    // --- Constructor ---
    public ZborDAO(ZborRepository zborRepository) {
        this.zborRepository = zborRepository;
    }

    // --- Metode ---

    @Override
    public List<Zbor> getZboruri() {
        return zborRepository.findAll().stream()
                .map(ZborEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Zbor> getZborById(int id) {
        return zborRepository.findById(id)
                .map(ZborEntity::toDomain);
    }

    @Override
    public List<Zbor> getZboruriByNumar(String numarZbor) {
        return zborRepository.findByNumarZborContainingIgnoreCase(numarZbor).stream()
                .map(ZborEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Zbor> getZboruriFiltrare(String plecare, String sosire, LocalDateTime start, LocalDateTime end) {
        // Trimite datele către query-ul personalizat din Repository și le întoarce ca Domain
        return zborRepository.filtreazaZboruri(plecare, sosire, start, end).stream()
                .map(ZborEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean insert(Zbor zbor) {
        ZborEntity entity = new ZborEntity(zbor);
        zborRepository.save(entity);
        return true;
    }

    @Override
    public boolean update(Zbor zbor) {
        if (zbor.getId() == null || !zborRepository.existsById(zbor.getId().getId())) {
            throw new IllegalArgumentException("Zborul pe care incercati sa il actualizati nu exista!");
        }

        ZborEntity entity = new ZborEntity(zbor);
        zborRepository.save(entity);
        return true;
    }

    @Override
    public boolean delete(int id) {
        if (!zborRepository.existsById(id)) {
            return false;
        }
        try {
            zborRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}