package com.aeroport.infrastructure;

import com.aeroport.infrastructure.tableEntities.BiletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Repository marchează această interfață ca fiind componenta de acces la date.
 * Moștenind JpaRepository, primim automat gratuit funcții precum save(), findById(), deleteById(), findAll()
 * fără să scriem noi niciun rând de cod SQL.
 */
@Repository
public interface BiletRepository extends JpaRepository<BiletEntity, Integer> {

    /**
     * Metodă custom. Spring Data JPA o implementează automat doar citindu-i numele ("findBy" + "IdZbor").
     * @param idZbor ID-ul zborului pentru care căutăm biletele.
     * @return O listă cu toate entitățile Bilet care aparțin de acel zbor.
     */
    List<BiletEntity> findByIdZbor(int idZbor);
}