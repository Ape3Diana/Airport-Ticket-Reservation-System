package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.EmployeeFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;

/**
 * Comandă utilizată pentru filtrarea/căutarea zborurilor după numărul de identificare unic.
 * Actualizează dinamic lista tabelară principală.
 */
public class SearchFlightsEmployeeCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final EmployeeFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public SearchFlightsEmployeeCommand(EmployeeFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        try {
            String cautare = viewModel.textCautareProperty().get().trim();

            // Validare defensivă: Nu interogăm rețeaua dacă string-ul de căutare este gol
            if (cautare.isEmpty()) {
                return;
            }

            // Solicitare date filtrate pe URL-ul specific de backend (/numar/{cautare})
            String url = restClient.getUrlZboruri() + "/numar/" + cautare;
            List<GenericDataModel> rezultateRaw = restClient.getListData(url);

            // Înlocuirea elementelor din tabel cu noile rezultate
            viewModel.actualizeazaListaZboruri(rezultateRaw);
            viewModel.mesajEroareProperty().set("");
        } catch (Exception e) {
            // Tratarea problemelor de parsare JSON sau lipsă conexiune microserviciu
            String eroareLimbaj = LanguageManager.getBundle().getString("employee.error.load_flights");
            viewModel.mesajEroareProperty().set(eroareLimbaj + " " + e.getMessage());
        }
    }
}