package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.FlightSeatMapViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.HashMap;
import java.util.Map;

/**
 * Comandă dedicată modificării detaliilor unui bilet existent (ex: corectare nume pasager).
 * Trimite modificările printr-o cerere HTTP PUT către sistemul central.
 */
public class UpdateBiletCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final FlightSeatMapViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public UpdateBiletCommand(FlightSeatMapViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        String numePasager = viewModel.numePasagerProperty().get().trim();

        // Validare obligativitate text nume complet în formularul hărții de locuri
        if (numePasager.isEmpty()) {
            viewModel.mesajStatusProperty().set(LanguageManager.getBundle().getString("seatmap.warn.empty_name_update"));
            return;
        }

        try {
            // Reconstituirea map-ului JSON complet pentru operațiunea de update general (PUT)
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", viewModel.getIdBiletCurent());
            payload.put("idZbor", viewModel.getIdZborCurent());
            payload.put("idUtilizator", 1);
            payload.put("numePasager", numePasager);
            payload.put("numarLoc", viewModel.numarLocProperty().get());
            payload.put("pretPlatit", viewModel.getPretCurent());

            // Re-formatarea datei într-un format compatibil cu standardul ISO primit de backend (înlocuire spațiu cu 'T')
            String dataVanzare = viewModel.dataVanzareProperty().get();
            if (!dataVanzare.isEmpty()) {
                payload.put("dataAchizitie", dataVanzare.replace(" ", "T"));
            }

            // Apelarea operațiunii PUT în rețea
            restClient.putData(restClient.getUrlBilete(), payload);

            // Reîncărcarea datelor proaspete și declanșarea evenimentului de re-desenare grafică
            viewModel.incarcaBilete();
            viewModel.triggerRefresh(LanguageManager.getBundle().getString("seatmap.success.update"));
        } catch (Exception e) {
            // Prinderea excepțiilor SQL sau REST și raportarea lor pe ecran
            viewModel.mesajStatusProperty().set(LanguageManager.getBundle().getString("seatmap.error.update") + " " + e.getMessage());
        }
    }
}