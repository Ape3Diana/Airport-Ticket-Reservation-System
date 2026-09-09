package com.aeroport.infrastructure;

import com.aeroport.domain.Notificare;
import com.aeroport.domain.StatusNotificare;
import com.aeroport.domain.dao.INotificareDAO;
import com.aeroport.infrastructure.tableEntities.NotificareEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementarea contractului INotificareDAO.
 * Aici aducem laolaltă Repository-ul de Spring și transformările între Domain și Entity.
 */
@Component
public class NotificareDAO implements INotificareDAO {
    private final NotificareRepository repo;

    // Injectarea dependenței repository-ului prin constructor
    public NotificareDAO(NotificareRepository repo) {
        this.repo = repo;
    }

    @Override
    public void insert(Notificare n) {
        // Transformăm obiectul curat într-o entitate JPA și o salvăm
        repo.save(new NotificareEntity(n));
    }

    @Override
    public List<Notificare> getNotificariInAsteptare(int idUtilizator) {
        // Scoatem entitățile din DB după statusul "IN_ASTEPTARE"
        return repo.findByIdUtilizatorAndStatusTrimitere(idUtilizator, StatusNotificare.IN_ASTEPTARE)
                .stream()
                .map(NotificareEntity::toDomain) // Transformăm lista de Entity-uri în lista de Domain
                .collect(Collectors.toList());
    }

    @Override
    public void marcheazaCaTrimise(int idUtilizator) {
        // Luăm toate entitățile necitite, le actualizăm statusul la TRIMIS, apoi le salvăm din nou
        List<NotificareEntity> notificari = repo.findByIdUtilizatorAndStatusTrimitere(idUtilizator, StatusNotificare.IN_ASTEPTARE);
        for (NotificareEntity ne : notificari) {
            ne.setStatusTrimitere(StatusNotificare.TRIMIS);
        }
        repo.saveAll(notificari);
    }
}