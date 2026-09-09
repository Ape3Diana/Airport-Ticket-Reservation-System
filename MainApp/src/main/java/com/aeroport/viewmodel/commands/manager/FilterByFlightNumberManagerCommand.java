package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Comandă ce implementează logica de filtrare locală/în memorie a zborurilor după numărul de zbor.
 * Permite interogarea rapidă a colecției descărcate utilizând Java Streams API.
 */
public class FilterByFlightNumberManagerCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public FilterByFlightNumberManagerCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică Stream
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();
        String numarCautat = viewModel.cautaDupaNumarProperty().get();

        try {
            // Obținerea întregului set de date din serviciu
            String url = restClient.getUrlZboruri();
            List<GenericDataModel> toateZborurile = restClient.getListData(url);

            // Verificare input: dacă textul e prezent aplicăm filtrul în flux, altfel reîncărcăm lista completă
            if (numarCautat != null && !numarCautat.trim().isEmpty()) {
                List<GenericDataModel> filtrate = toateZborurile.stream()
                        .filter(zbor -> !zbor.getInfo().isEmpty() &&
                                zbor.getInfo().get(0).toLowerCase().contains(numarCautat.toLowerCase()))
                        .collect(Collectors.toList());
                viewModel.actualizeazaTabel(filtrate);
            } else {
                viewModel.actualizeazaTabel(toateZborurile);
            }

            viewModel.statusProperty().set(bundle.getString("status.flights_loaded"));
        } catch (Exception e) {
            // Raportarea inline a problemelor de conectare la nivel de tabel
            viewModel.statusProperty().set(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}