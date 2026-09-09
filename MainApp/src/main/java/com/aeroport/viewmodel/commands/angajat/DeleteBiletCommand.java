package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.FlightSeatMapViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă utilizată pentru anularea (ștergerea) unui bilet din sistem.
 * Comunică cu microserviciul REST pentru a elimina înregistrarea pe baza ID-ului selectat.
 */
public class DeleteBiletCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    /** ViewModel-ul hărții de locuri, utilizat pentru a extrage ID-ul biletului curent și pentru reîmprospătare. */
    private final FlightSeatMapViewModel viewModel;

    /** Clientul de rețea folosit pentru transmiterea cererilor HTTP DELETE către microserviciu. */
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public DeleteBiletCommand(FlightSeatMapViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    @Override
    public void execute() {
        try {
            // Trimite cererea HTTP DELETE către backend pentru biletul curent selectat
            restClient.deleteData(restClient.getUrlBilete() + "/" + viewModel.getIdBiletCurent());

            // Reîncarcă starea biletelor și declanșează actualizarea matricei vizuale din interfață
            viewModel.incarcaBilete();
            viewModel.triggerRefresh(LanguageManager.getBundle().getString("seatmap.success.cancel"));
        } catch (Exception e) {
            // În caz de eroare, mesajul este trimis direct în proprietatea de status din interfață
            viewModel.mesajStatusProperty().set(LanguageManager.getBundle().getString("seatmap.error.cancel") + " " + e.getMessage());
        }
    }
}