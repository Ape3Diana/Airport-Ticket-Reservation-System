package com.aeroport.infrastructure;

import com.aeroport.infrastructure.tableEntities.ZborEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Generat pe bază de interfețe, Spring Data JPA construiește automat
 * în spate implementările de SQL folosind Hibernate.
 */

@Repository
public interface ZborRepository extends JpaRepository<ZborEntity, Integer> {

    // Query personalizat (JPQL) care face căutare flexibilă pe text (oraș/cod IATA) folosind clauze LIKE standardizate.
    // Verifică în plus dacă zborul se încadrează în intervalul temporal al zilei alese.
    @Query("SELECT z FROM ZborEntity z WHERE " +
            "(LOWER(z.aeroportPlecare.oras) LIKE LOWER(CONCAT('%', :plecare, '%')) OR " +
            " LOWER(z.aeroportPlecare.codIata) LIKE LOWER(CONCAT('%', :plecare, '%'))) AND " +
            "(LOWER(z.aeroportSosire.oras) LIKE LOWER(CONCAT('%', :sosire, '%')) OR " +
            " LOWER(z.aeroportSosire.codIata) LIKE LOWER(CONCAT('%', :sosire, '%'))) AND " +
            "z.oraDecolare >= :startOfDay AND z.oraDecolare < :endOfDay")
    List<ZborEntity> filtreazaZboruri(@Param("plecare") String plecare,
                                      @Param("sosire") String sosire,
                                      @Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);

    // Query generat dinamic de Spring pe baza numelui metodei:
    // Caută zborul după string ignorând case-ul literelor (ex: "ro" va găsi "RO301" și "ro202")
    List<ZborEntity> findByNumarZborContainingIgnoreCase(String numarZbor);
}