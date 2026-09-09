package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Comandă utilizată de Manager pentru încărcarea inițială totală a zborurilor din sistem.
 * Actualizează tabelul complex al panoului de management de zboruri.
 */
public class LoadAllFlightsManagerCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public LoadAllFlightsManagerCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();
        try {
            // Solicită lista completă din microserviciul de rețea
            String url = restClient.getUrlZboruri();
            List<GenericDataModel> toateZborurile = restClient.getListData(url);

            // Populează tabelul grafic și notifică starea prin status label
            viewModel.actualizeazaTabel(toateZborurile);
            viewModel.statusProperty().set(bundle.getString("status.flights_loaded"));
        } catch (Exception e) {
            // Declanșează modalul securizat în caz de eșec rețea (Server oprit / Gateway Error)
            viewModel.triggerError(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}