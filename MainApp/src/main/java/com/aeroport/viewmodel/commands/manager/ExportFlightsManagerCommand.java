package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Comandă asincronă ce permite managerului să exporte structura curentă a tabelului de zboruri.
 * Respectă filtrele aplicate pe ecran și descarcă fișierul generat în directorul local dedicate resurselor.
 */
public class ExportFlightsManagerCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public ExportFlightsManagerCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Asincronă
    // =========================================================================

    @Override
    public void execute() {
        String format = viewModel.formatExportProperty().get();
        ResourceBundle bundle = LanguageManager.getBundle();

        // Rulăm asincron pentru a asigura un management binar fluid, fără blocaje de interfață
        CompletableFuture.runAsync(() -> {
            try {
                // 1. Preluăm datele exact așa cum sunt afișate pe ecran în acest moment (cu filtre incluse)
                List<List<String>> zboruriTabel = viewModel.getListaZboruriAfisare();

                if (zboruriTabel.isEmpty()) {
                    Platform.runLater(() -> viewModel.triggerError(bundle.getString("export.error.no_flights")));
                    return;
                }

                // 2. Formatarea listei sub formă de perechi cheie-valoare ordonate pentru microserviciul de mapare
                List<Map<String, Object>> dateExport = new ArrayList<>();
                for (List<String> rand : zboruriTabel) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("Număr Zbor", rand.get(1));
                    map.put("Plecare", rand.get(2));
                    map.put("Sosire", rand.get(3));
                    map.put("Data/Ora Decolare", rand.get(4));
                    map.put("Data/Ora Aterizare", rand.get(5));
                    map.put("Preț (EUR)", rand.get(6));
                    map.put("Locuri Disponibile", rand.get(7));
                    dateExport.add(map);
                }

                // 3. Descărcarea matricei de octeți corespunzătoare formatului cerut
                String exportUrl = restClient.getUrlExport() + "/" + format;
                byte[] fisierGenerat = restClient.downloadBinaryData(exportUrl, dateExport);

                // 4. Crearea structurii de directoare dedicată managerului
                Path folderExport = Paths.get("src/main/resources/manager_exports");
                Files.createDirectories(folderExport);

                // Configurare denumire fișier cu timestamp secundar unic (dd-MM-yyyy_HH-mm-ss)
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String timestamp = LocalDateTime.now().format(dtf);
                String cuvantZboruri = bundle.getString("export.filename.flights");

                String numeFisier = "export_" + cuvantZboruri + "_" + timestamp + "." + format;
                Path caleCompleta = folderExport.resolve(numeFisier);

                // Salvarea binară fizică a raportului de zboruri descărcat
                Files.write(caleCompleta, fisierGenerat);

                // 5. Trimiterea confirmării în eticheta de status din thread-ul principal de UI
                Platform.runLater(() -> {
                    String msg = bundle.getString("export.success.message");
                    viewModel.statusProperty().set(msg.replace("%s", numeFisier));
                });

            } catch (Exception e) {
                // Tratarea erorilor I/O sau HTTP și propagarea lor prin intermediul pop-up-ului de eroare
                Platform.runLater(() -> {
                    viewModel.triggerError(bundle.getString("export.error.generic") + ": " + e.getMessage());
                });
            }
        });
    }
}