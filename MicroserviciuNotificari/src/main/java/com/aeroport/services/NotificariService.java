package com.aeroport.services;

import com.aeroport.domain.Notificare;
import com.aeroport.domain.TipCanal;
import com.aeroport.domain.dao.INotificareDAO;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Clasa de Serviciu care conține logica de business pentru notificări.
 */
@Service
public class NotificariService {

    // Dependența doar de contract (interfață), nu de implementarea bazei de date. Asta ajută enorm la testare!
    private final INotificareDAO notificareDAO;

    public NotificariService(INotificareDAO notificareDAO) {
        this.notificareDAO = notificareDAO;
    }

    /**
     * Logică declanșată la modificarea setărilor de cont (ex: schimbare parolă).
     * Creează 3 tipuri distincte de alerte pentru siguranță.
     */
    public void notificaModificareCont(int idUtilizator, String mesaj) {
        // 1. Generăm obiectele pentru cele 3 canale distincte (statusul "IN_ASTEPTARE" se pune automat prin constructor)
        Notificare email = new Notificare(null, idUtilizator, TipCanal.EMAIL, "Securitate Cont", mesaj, null, null);
        Notificare sms = new Notificare(null, idUtilizator, TipCanal.SMS, "Alerta SMS", mesaj, null, null);
        Notificare whatsapp = new Notificare(null, idUtilizator, TipCanal.WHATSAPP, "Alerta WhatsApp", mesaj, null, null);

        // 2. Le trimitem către stratul de persistență (DAO) pentru a fi salvate în DB
        notificareDAO.insert(email);
        notificareDAO.insert(sms);
        notificareDAO.insert(whatsapp);
    }

    /**
     * Funcție folosită pentru a prelua mesajele pending ale unui utilizator (ex: la logare)
     * și pentru a actualiza imediat statusul lor ca să nu fie arătate de două ori.
     */
    public List<Notificare> getSipreiaNotificariAsteptare(int idUtilizator) {
        // 1. Extragem lista notificărilor care sunt în așteptare
        List<Notificare> notificari = notificareDAO.getNotificariInAsteptare(idUtilizator);

        // 2. Dacă am găsit alerte necitite, apelăm imediat funcția de schimbare a statusului în "TRIMIS"
        if (!notificari.isEmpty()) {
            notificareDAO.marcheazaCaTrimise(idUtilizator);
        }

        // 3. Returnăm alertele (în forma lor inițială, înainte de update) pentru a fi trimise către client
        return notificari;
    }
}