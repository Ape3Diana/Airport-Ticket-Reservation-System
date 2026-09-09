package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.AdminUserManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.ResourceBundle;

/**
 * Comandă responsabilă de ștergerea unui utilizator din sistem.
 * Interacționează cu serviciul REST pentru a elimina entitatea pe baza ID-ului selectat.
 */
public class DeleteUserCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    /** ViewModel-ul de gestiune utilizatori, folosit pentru citirea stării și raportarea succesului/erorilor. */
    private final AdminUserManagementViewModel viewModel;

    /** Clientul de rețea folosit pentru transmiterea cererilor HTTP DELETE către microserviciu. */
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    /**
     * Construiește o nouă instanță a comenzii de ștergere.
     * * @param viewModel instanța de stocare a stării UI-ului
     * @param restClient clientul pentru comunicația backend
     */
    public DeleteUserCommand(AdminUserManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică (Business Core)
    // =========================================================================

    /**
     * Execută procesul de ștergere a utilizatorului curent selectat în tabel.
     * Validează existența selecției, apelează serviciul REST și reîmprospătează interfața grafică.
     */
    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();
        String idSelectat = viewModel.getIdSelectat();

        // Validare defensivă: Verificăm dacă administratorul a selectat un utilizator din tabel
        if (idSelectat == null || idSelectat.isEmpty()) {
            viewModel.triggerError(bundle.getString("error.select_delete"));
            return;
        }

        try {
            // Construirea URL-ului specific resursei și trimiterea cererii DELETE
            String url = restClient.getUrlUtilizatori() + "/" + idSelectat;
            restClient.deleteData(url);

            // Notificarea UI-ului, reîncărcarea listei din backend și curățarea câmpurilor din formular
            viewModel.triggerSuccess(bundle.getString("status.user_deleted"));
            viewModel.incarcaUtilizatori();
            viewModel.curataFormular();

        } catch (Exception e) {
            // Prinderea și propagarea erorilor de conexiune sau restricții de integritate direct în pop-up
            viewModel.triggerError(e.getMessage());
        }
    }
}