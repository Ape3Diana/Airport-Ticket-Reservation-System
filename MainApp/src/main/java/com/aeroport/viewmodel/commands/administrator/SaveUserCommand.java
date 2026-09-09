package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.AdminUserManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Comandă complexă responsabilă de persistența datelor unui utilizator (Operațiuni de Create și Update).
 * Include validarea inputurilor, detectarea fină a modificărilor de câmpuri (Audit delta)
 * și transmiterea de alerte asincrone de securitate prin email/SMS către utilizatorul final modificat.
 */
public class SaveUserCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final AdminUserManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public SaveUserCommand(AdminUserManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică (Salvare / Audit / Notificare)
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();

        // Identificarea modului de rulare: adăugare entitate nouă sau actualizare entitate existentă
        String idSelectat = viewModel.getIdSelectat();
        boolean isUserNou = (idSelectat == null || idSelectat.isEmpty());

        // 1. Validare defensivă a obligativității tuturor câmpurilor textuale din formular
        if (viewModel.emailProperty().get().isEmpty() ||
                viewModel.rolSelectatProperty().get() == null ||
                viewModel.numeCompletProperty().get().isEmpty() ||
                viewModel.parolaProperty().get().trim().isEmpty() ||
                viewModel.telefonProperty().get().trim().isEmpty()) {

            viewModel.triggerError(bundle.getString("error.fields"));
            return;
        }

        try {
            // 2. Maparea payload-ului JSON brut pentru trimiterea spre server
            java.util.Map<String, Object> userData = new java.util.HashMap<>();
            userData.put("email", viewModel.emailProperty().get());
            userData.put("parola", viewModel.parolaProperty().get());
            userData.put("numeComplet", viewModel.numeCompletProperty().get());
            userData.put("telefon", viewModel.telefonProperty().get());
            userData.put("tipUtilizator", viewModel.getBackendRole(viewModel.rolSelectatProperty().get()));

            String url = restClient.getUrlUtilizatori();

            if (isUserNou) {
                // ==========================================
                // SUB-PROCES: INSERARE UTILIZATOR NOU (POST)
                // ==========================================
                restClient.postData(url, userData);
                viewModel.triggerSuccess(bundle.getString("status.user_saved"));
            } else {
                // ===========================================
                // SUB-PROCES: ACTUALIZARE EXISTENTĂ (PUT)
                // ===========================================
                java.util.Map<String, Object> idMap = new java.util.HashMap<>();
                int userId = Integer.parseInt(idSelectat);
                idMap.put("id", userId);
                userData.put("id", idMap);

                // Executarea operațiunii PUT în sistemul centralizat
                restClient.putData(url, userData);

                // --- DETERMINARE ELEMENTE MODIFICATE (DELTA AUDIT LOGIC) ---
                List<String> dateVechi = viewModel.selectedUserProperty().get();
                StringBuilder modificari = new StringBuilder();

                // Verificare modficiari Email
                String vechiEmail = dateVechi.get(1);
                String nouEmail = viewModel.emailProperty().get();
                if (!vechiEmail.equals(nouEmail)) {
                    modificari.append("• Email: ").append(vechiEmail).append(" -> ").append(nouEmail).append("\n");
                }

                // Verificare modificari Nume Complet
                String vechiNume = dateVechi.get(2);
                String nouNume = viewModel.numeCompletProperty().get();
                if (!vechiNume.equals(nouNume)) {
                    modificari.append("• Nume: ").append(vechiNume).append(" -> ").append(nouNume).append("\n");
                }

                // Verificare modificari Rol administrativ
                String vechiRol = dateVechi.get(3);
                String nouRol = viewModel.rolSelectatProperty().get();
                if (!vechiRol.equals(nouRol)) {
                    modificari.append("• Rol: ").append(vechiRol).append(" -> ").append(nouRol).append("\n");
                }

                // Verificare modificari Număr Telefon
                String vechiTelefon = dateVechi.get(4);
                String nouTelefon = viewModel.telefonProperty().get();
                if (!vechiTelefon.equals(nouTelefon)) {
                    modificari.append("• Telefon: ").append(vechiTelefon).append(" -> ").append(nouTelefon).append("\n");
                }

                // Verificare modificari Parolă (Afișată securizat în clar conform cerinței)
                String vechiParola = dateVechi.get(5);
                String nouParola = viewModel.parolaProperty().get();
                if (!vechiParola.equals(nouParola)) {
                    modificari.append("• Parolă: ").append(vechiParola).append(" -> ").append(nouParola).append("\n");
                }

                // 3. Procesarea alertelor push de securitate doar în prezența unei delte reale
                if (modificari.length() > 0) {
                    // Mesajul securizat generat pe e-mail/SMS către utilizator păstrează formatarea fixă de bază
                    String mesajNotificare = "Administratorul v-a modificat datele contului.\n\nS-au făcut următoarele modificări:\n" + modificari.toString();
                    restClient.trimiteAlertaSecuritate(userId, mesajNotificare);

                    // Reîmprospătare text status local tradus pentru limba curentă a adminului
                    viewModel.triggerSuccess(bundle.getString("status.user_saved") + bundle.getString("admin.user.notify.success"));
                } else {
                    viewModel.triggerSuccess(bundle.getString("status.user_saved") + bundle.getString("admin.user.notify.no_changes"));
                }
            }

            // Reîmprospătarea listei tabulare de utilizatori și golirea câmpurilor
            viewModel.incarcaUtilizatori();
            viewModel.curataFormular();

        } catch (Exception e) {
            // 4. Tratare inteligentă a excepțiilor venite de la constrângerile unice ale bazei de date SQL (E-mail/Telefon duplicat)
            String msgServer = e.getMessage();
            String mesajFinal = msgServer;

            if (msgServer != null) {
                if (msgServer.toLowerCase().contains("telefon")) {
                    mesajFinal = bundle.getString("error.duplicate.phone");
                } else if (msgServer.toLowerCase().contains("email")) {
                    mesajFinal = bundle.getString("error.duplicate.email");
                }
            }
            viewModel.triggerError(bundle.getString("error.save") + " " + mesajFinal);
        }
    }
}