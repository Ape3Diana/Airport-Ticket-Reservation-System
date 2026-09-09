package com.aeroport.viewmodel.commands.manager;

import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.ManagerStatsViewModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Comandă asincronă responsabilă de descărcarea datelor analitice complexe (Venituri, Ocupare, Destinații).
 * Populează structurile de tip map din ViewModel și notifică ecranul pentru re-redesenarea graficelor.
 */
public class LoadStatsCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerStatsViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public LoadStatsCommand(ManagerStatsViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Asincronă în Fundal (Analytics Fetcher)
    // =========================================================================

    @Override
    public void execute() {
        // Separăm procesul intensiv de interogare pe thread secundar dedicat
        CompletableFuture.runAsync(() -> {
            try {
                String baseUrl = restClient.getUrlStatistici();

                // 1. Descărcarea datelor istorice pentru Graficul de Venituri
                List<Map<String, Object>> venituri = restClient.getListMapData(baseUrl + "/venituri");
                viewModel.getDataVenituri().clear();
                viewModel.getDataVenituri().addAll(venituri);

                // 2. Descărcarea datelor analitice pentru Graficul de Ocupare pe Rute
                List<Map<String, Object>> ocupare = restClient.getListMapData(baseUrl + "/ocupare");
                viewModel.getDataOcupare().clear();
                viewModel.getDataOcupare().addAll(ocupare);

                // 3. Descărcarea ponderilor pentru Graficul de tip plăcintă (Destinații)
                List<Map<String, Object>> destinatii = restClient.getListMapData(baseUrl + "/destinatii");
                viewModel.getDataDestinatii().clear();
                viewModel.getDataDestinatii().addAll(destinatii);

                // Declanșarea callback-ului de redesenare grafică în View (populeazaGrafice())
                viewModel.notificaDateIncarcate();

            } catch (Exception e) {
                // Trimiterea pop-up-ului de avertizare în caz de prăbușire a sistemului de BI/Analytics
                viewModel.triggerError(LanguageManager.getBundle().getString("export.error.generic") + ": " + e.getMessage());
            }
        });
    }
}