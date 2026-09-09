package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.ResourceBundle;

/**
 * Comandă din pachetul manager responsabilă de ștergerea unui zbor selectat.
 * Apelează serviciul DELETE, curăță formularul și reîncarcă automat grila.
 */
public class DeleteFlightCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public DeleteFlightCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();
        String id = viewModel.getIdSelectat();

        // Validare defensivă a existenței unei selecții valide înainte de apelul de rețea
        if (id == null || id.trim().isEmpty()) {
            viewModel.triggerError(bundle.getString("error.no_selection"));
            return;
        }

        try {
            // Executarea cererii HTTP DELETE către endpoint-ul de zboruri
            restClient.deleteData(restClient.getUrlZboruri() + "/" + id);

            // Transmiterea mesajului de succes în proprietatea de status inline a managerului
            viewModel.statusProperty().set(bundle.getString("status.delete_success"));

            // Resetarea formularului și reîmprospătarea listei tabulare globale
            viewModel.curataFormular();
            viewModel.getLoadAllFlightsManagerCommand().execute();

        } catch (Exception e) {
            // Capturarea erorilor de rețea și trimiterea lor spre modalul de alertă
            viewModel.triggerError(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}