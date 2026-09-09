package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.EmployeeFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;

/**
 * Comandă responsabilă de încărcarea inițială și completă a tuturor zborurilor din sistem.
 * Populează tabelul principal de lucru al angajatului.
 */
public class LoadAllFlightsEmployeeCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final EmployeeFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public LoadAllFlightsEmployeeCommand(EmployeeFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        try {
            // Solicită lista brută de modele generice de date de la endpoint-ul principal de zboruri
            String url = restClient.getUrlZboruri();
            List<GenericDataModel> toateZborurile = restClient.getListData(url);

            // Trimite datele spre decapsulare și populare în colecția observabilă din ViewModel
            viewModel.actualizeazaListaZboruri(toateZborurile);
            viewModel.mesajEroareProperty().set("");
        } catch (Exception e) {
            // Prinderea erorilor de conexiune HTTP și traducerea dinamică a mesajului afișat
            String eroareLimbaj = LanguageManager.getBundle().getString("employee.error.load_flights");
            viewModel.mesajEroareProperty().set(eroareLimbaj + " " + e.getMessage());
        }
    }
}