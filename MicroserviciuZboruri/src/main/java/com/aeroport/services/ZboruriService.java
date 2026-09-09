package com.aeroport.services;

import com.aeroport.domain.Aeroport;
import com.aeroport.domain.Zbor;
import com.aeroport.domain.dao.IAeroportDAO;
import com.aeroport.domain.dao.IZborDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Coordonează regulile de business. Nu știe detalii despre cum arată tabelele din baza de date,
 * ci operează direct cu modelele de domeniu pure (Zbor, Aeroport).
 */

@Service
public class ZboruriService {

    // --- Atribute ---
    // Interfețele din Domain (Porturile) folosite pentru accesarea datelor. Absorb dependența directă de baza de date.
    private final IZborDAO zborDAO;
    private final IAeroportDAO aeroportDAO;

    // --- Constructor ---
    // Injectarea automată de către Spring a implementărilor din infrastructură (Adaptoarele DAO)
    public ZboruriService(IZborDAO zborDAO, IAeroportDAO aeroportDAO) {
        this.zborDAO = zborDAO;
        this.aeroportDAO = aeroportDAO;
    }

    // --- Metode Core/CRUD ---

    public List<Zbor> getZboruri() {
        return zborDAO.getZboruri();
    }

    public Optional<Zbor> getZborById(int id) {
        return zborDAO.getZborById(id);
    }

    public List<Zbor> getZboruriByNumar(String numarZbor) {
        return zborDAO.getZboruriByNumar(numarZbor);
    }

    public List<Zbor> getZboruriFiltrare(String plecare, String sosire, LocalDateTime start, LocalDateTime end) {
        return zborDAO.getZboruriFiltrare(plecare, sosire, start, end);
    }

    public boolean insertZbor(Zbor zbor) {
        return zborDAO.insert(zbor);
    }

    public boolean updateZbor(Zbor zbor) {
        return zborDAO.update(zbor);
    }

    public boolean deleteZbor(int id) {
        return zborDAO.delete(id);
    }

    // --- Metode cu Logică de Business Complexă ---

    // Gestionează scăderea locului din stoc la achiziția unui bilet
    public boolean rezervaBilet(int idZbor) {
        Optional<Zbor> zborOpt = zborDAO.getZborById(idZbor);

        if (zborOpt.isPresent()) {
            Zbor zbor = zborOpt.get();

            // 1. Verifică în interiorul entității de domeniu dacă mai sunt locuri disponibile
            if (zbor.areLocuriDisponibile()) {
                // 2. Modifică starea obiectului (scade loc disponibil)
                zbor.scadeLocDisponibil();
                // 3. Salvează noul status în baza de date
                return zborDAO.update(zbor);
            } else {
                throw new IllegalStateException("Zborul este SOLD OUT. Nu mai sunt locuri disponibile.");
            }
        }
        throw new IllegalArgumentException("Zborul specificat nu exista in sistem.");
    }

    // Gestionează adăugarea locului înapoi când un bilet este anulat/șters
    public boolean elibereazaLoc(int idZbor) {
        Optional<Zbor> zborOpt = zborDAO.getZborById(idZbor);

        if (zborOpt.isPresent()) {
            Zbor zbor = zborOpt.get();
            // Invocă comportamentul de domeniu dedicat suplimentării de locuri
            zbor.adaugaLocDisponibil();
            return zborDAO.update(zbor);
        }
        throw new IllegalArgumentException("Zborul specificat nu exista in sistem.");
    }

    // Returnează lista completă de aeroporturi din sistem
    public List<Aeroport> getAeroporturi() {
        return aeroportDAO.getAeroporturi();
    }
}