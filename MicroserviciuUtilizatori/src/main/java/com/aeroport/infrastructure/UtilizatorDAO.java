package com.aeroport.infrastructure;

import com.aeroport.domain.TipRol;
import com.aeroport.domain.Utilizator;
import com.aeroport.domain.dao.IUtilizatorDAO;
import com.aeroport.infrastructure.tableEntities.UtilizatorEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementarea contractului IUtilizatorDAO folosind repository-ul Spring (JPA).
 * Transformă entitățile bazei de date înapoi în obiecte de Domeniu și invers.
 */
@Component
public class UtilizatorDAO implements IUtilizatorDAO {

    private final UtilizatorRepository utilizatorRepository;

    public UtilizatorDAO(UtilizatorRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    @Override
    public List<Utilizator> getUtilizatori() {
        // Preia toate entitățile și le mapează (toDomain) într-o listă de obiecte Utilizator
        return utilizatorRepository.findAll().stream()
                .map(UtilizatorEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Utilizator> getUtilizatorById(int id) {
        // Caută după ID. Dacă găsește, îl mapează spre Domain. Returnează Optional (poate fi gol).
        return utilizatorRepository.findById(id).map(UtilizatorEntity::toDomain);
    }

    @Override
    public Optional<Utilizator> getUtilizatorByEmail(String email) {
        return utilizatorRepository.findByEmail(email).map(UtilizatorEntity::toDomain);
    }

    @Override
    public List<Utilizator> getUtilizatoriFilterByTip(TipRol tip) {
        return utilizatorRepository.findByTipUtilizator(tip).stream()
                .map(UtilizatorEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean insert(Utilizator utilizator) {
        // Convertește din Domain în Entity și salvează
        UtilizatorEntity entity = new UtilizatorEntity(utilizator);
        utilizatorRepository.save(entity);
        return true;
    }

    @Override
    public boolean update(Utilizator utilizator) {
        // Siguranță: Verifică dacă utilizatorul pe care vrem să-l updatăm chiar există în baza de date
        if (utilizator.getId() == null || !utilizatorRepository.existsById(utilizator.getId().getId())) {
            throw new IllegalArgumentException("Eroare: Utilizatorul nu a fost găsit pentru actualizare.");
        }
        UtilizatorEntity entity = new UtilizatorEntity(utilizator);
        utilizatorRepository.save(entity); // Funcția save face un "UPDATE" dacă ID-ul există deja
        return true;
    }

    @Override
    public boolean delete(int id) {
        // Verificăm dacă există înainte de a încerca să-l ștergem
        if (!utilizatorRepository.existsById(id)) {
            return false;
        }
        utilizatorRepository.deleteById(id);
        return true;
    }
}