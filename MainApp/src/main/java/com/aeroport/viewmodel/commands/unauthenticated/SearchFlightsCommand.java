package com.aeroport.viewmodel.commands.unauthenticated;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.UnauthenticatedFlightViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.ResourceBundle;

public class SearchFlightsCommand implements ICommand {
    private final UnauthenticatedFlightViewModel viewModel;
    private final RestServiceClient restClient;

    public SearchFlightsCommand(UnauthenticatedFlightViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();
        String from = viewModel.plecareProperty().get();
        String to = viewModel.sosireProperty().get();
        java.time.LocalDate date = viewModel.dataZborProperty().get();

        if (from == null || from.trim().isEmpty() || to == null || to.trim().isEmpty() || date == null) {
            viewModel.triggerErrorPopup(bundle.getString("error.fields"));
            return;
        }

        try {
            String url = restClient.getUrlZboruri() + "/search?from=" + from + "&to=" + to + "&date=" + date.toString();
            List<GenericDataModel> rezultateRaw = restClient.getListData(url);

            viewModel.actualizeazaListaZboruri(rezultateRaw);

            if (rezultateRaw.isEmpty()) {
                viewModel.afiseazaMesajStatus(bundle.getString("error.no_flights"));
            } else {
                viewModel.afiseazaMesajStatus(""); // Curăță eroarea
            }
        } catch (Exception e) {
            viewModel.triggerErrorPopup(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}