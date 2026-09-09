package com.aeroport.infrastructure;

import com.aeroport.infrastructure.tableEntities.AeroportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Generat pe bază de interfețe, Spring Data JPA construiește automat în spate
 * implementările de SQL folosind Hibernate.
 */

@Repository
// JpaRepository primește tipul Entității și tipul cheii primare (AeroportEntity, Integer)
public interface AeroportRepository extends JpaRepository<AeroportEntity, Integer> {
    // Moștenește metodele standard: findAll(), findById(), save(), deleteById()
}