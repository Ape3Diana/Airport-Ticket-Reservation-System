package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.FlightSeatMapViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.HashMap;
import java.util.Map;

/**
 * Comandă responsabilă de procesarea vânzării unui bilet nou (Achiziție).
 * Trimite payload-ul structurat cu numele pasagerului și scaunul ales către microserviciu.
 */
public class SaveBiletCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final FlightSeatMapViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public SaveBiletCommand(FlightSeatMapViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        String numePasager = viewModel.numePasagerProperty().get().trim();

        // Validare locală elementară: Câmpul numelui pasagerului este obligatoriu la vânzare
        if (numePasager.isEmpty()) {
            viewModel.mesajStatusProperty().set(LanguageManager.getBundle().getString("seatmap.warn.empty_name_sell"));
            return;
        }

        try {
            // Construirea obiectului payload asociat biletului vândut
            Map<String, Object> payload = new HashMap<>();
            payload.put("idZbor", viewModel.getIdZborCurent());
            payload.put("idUtilizator", 1); // Hardcodat conform stării sistemului curent
            payload.put("numePasager", numePasager);
            payload.put("numarLoc", viewModel.numarLocProperty().get());
            payload.put("pretPlatit", viewModel.getPretCurent());

            // Transmiterea biletului în baza de date prin rețeaua HTTP POST
            restClient.postData(restClient.getUrlBilete(), payload);

            // Reîmprospătare cache intern și alertarea matricei vizuale din View (Scaunul devine ROȘU / Ocupat)
            viewModel.incarcaBilete();
            viewModel.triggerRefresh(LanguageManager.getBundle().getString("seatmap.success.sell"));
        } catch (Exception e) {
            // Afișarea defecțiunii direct pe zona de alertare inline a ecranului scaunelor
            viewModel.mesajStatusProperty().set(LanguageManager.getBundle().getString("seatmap.error.sell") + " " + e.getMessage());
        }
    }
}