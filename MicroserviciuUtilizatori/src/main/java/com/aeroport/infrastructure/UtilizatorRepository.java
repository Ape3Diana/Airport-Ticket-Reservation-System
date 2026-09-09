package com.aeroport.infrastructure;

import com.aeroport.domain.TipRol;
import com.aeroport.infrastructure.tableEntities.UtilizatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfață Spring Data JPA care generează automat interogările SQL (CRUD).
 * Extinde JpaRepository primind tipul entității (UtilizatorEntity) și tipul ID-ului (Integer).
 */
@Repository
public interface UtilizatorRepository extends JpaRepository<UtilizatorEntity, Integer> {

    // Spring Data construiește automat query-ul: SELECT * FROM utilizator WHERE email = ?
    Optional<UtilizatorEntity> findByEmail(String email);

    // Spring Data construiește automat query-ul: SELECT * FROM utilizator WHERE tip_utilizator = ?
    List<UtilizatorEntity> findByTipUtilizator(TipRol tipUtilizator);
}