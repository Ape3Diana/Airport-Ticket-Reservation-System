package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Comandă din pachetul manager responsabilă de căutarea avansată și strictă a zborurilor în baza de date.
 * Interoghează serverul folosind parametrii de query (from, to, date) citiți direct din panoul de filtrare.
 */
public class SearchFlightsManagerCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public SearchFlightsManagerCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Interogare Server (Advanced Search)
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();

        // Colectarea valorilor introduse în câmpurile specifice de filtrare din interfață
        String from = viewModel.plecareCautaProperty().get();
        String to = viewModel.sosireCautaProperty().get();
        java.time.LocalDate date = viewModel.dataCautaProperty().get();

        // Validare strictă obligatorie: toate cele 3 criterii sunt mandatorii pentru acest endpoint complex
        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty() || date == null) {
            viewModel.triggerError(bundle.getString("error.fields"));
            return;
        }

        try {
            // Construirea URL-ului final cu Query Parameters conform specificației REST din microserviciu
            String url = restClient.getUrlZboruri() + "/search?from=" + from + "&to=" + to + "&date=" + date.toString();
            List<GenericDataModel> rezultateRaw = restClient.getListData(url);

            // Actualizarea tabelului cu rezultatele întoarse de microserviciu
            viewModel.actualizeazaTabel(rezultateRaw);

            // Verificare rezultate pentru raportarea stării potrivite în eticheta de status inline
            if (rezultateRaw.isEmpty()) {
                viewModel.statusProperty().set(bundle.getString("error.no_flights"));
            } else {
                viewModel.statusProperty().set(bundle.getString("status.flights_loaded"));
            }
        } catch (Exception e) {
            // Prinderea erorilor și propagarea lor prin modalul securizat al interfeței grafice
            viewModel.triggerError(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}