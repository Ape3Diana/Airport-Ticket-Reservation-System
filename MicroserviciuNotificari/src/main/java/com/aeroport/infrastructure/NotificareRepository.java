package com.aeroport.infrastructure;

import com.aeroport.domain.StatusNotificare;
import com.aeroport.infrastructure.tableEntities.NotificareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interfața magică Spring Data JPA care ne scutește de a scrie query-uri SQL manuale.
 */
public interface NotificareRepository extends JpaRepository<NotificareEntity, Integer> {

    // Spring generează SQL-ul automat pe baza numelui metodei:
    // SELECT * FROM notificare WHERE id_utilizator = ? AND status_trimitere = ?
    List<NotificareEntity> findByIdUtilizatorAndStatusTrimitere(int idUtilizator, StatusNotificare status);
}