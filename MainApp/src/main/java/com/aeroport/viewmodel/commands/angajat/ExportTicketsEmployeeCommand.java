package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.EmployeeFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Comandă utilizată de angajați pentru a exporta lista completă de bilete a unui zbor.
 * Suportă formatarea dinamică configurată în ViewModel (csv, json, xml, doc).
 */
public class ExportTicketsEmployeeCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final EmployeeFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public ExportTicketsEmployeeCommand(EmployeeFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Asincronă
    // =========================================================================

    @Override
    public void execute() {
        int idZbor = viewModel.idZborSelectatProperty().get();
        String numarZbor = viewModel.numarZborSelectatProperty().get();
        String format = viewModel.formatExportProperty().get();

        // Validare: Dacă nu s-a selectat niciun zbor valid, anulăm operațiunea
        if (idZbor == -1 || numarZbor.isEmpty()) return;

        ResourceBundle bundle = LanguageManager.getBundle();

        // Executăm logica pe un fir de fundal pentru a preveni blocarea animațiilor de UI
        CompletableFuture.runAsync(() -> {
            try {
                // 1. Preluăm colecția brută de bilete aferente zborului de la microserviciu
                List<Map<String, Object>> bilete = restClient.getBileteZborAsMap(idZbor);

                if (bilete.isEmpty()) {
                    Platform.runLater(() -> {
                        viewModel.mesajEroareProperty().set(bundle.getString("export.error.no_tickets"));
                        viewModel.mesajSuccesProperty().set("");
                    });
                    return;
                }

                // 2. Apelăm microserviciul de conversie în formatul solicitat (csv/json/xml/doc)
                String exportUrl = restClient.getUrlExport() + "/" + format;
                byte[] fisierGenerat = restClient.downloadBinaryData(exportUrl, bilete);

                // 3. Configurăm folderul destinație în resursele aplicației
                Path folderExport = Paths.get("src/main/resources/employee_exports");
                Files.createDirectories(folderExport);

                // 4. Construim numele fișierului pe baza traducerilor curente și a timpului
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm");
                String dataExport = LocalDateTime.now().format(dtf);
                String cuvantBilete = bundle.getString("export.filename.tickets");

                String numeFisier = "export_" + cuvantBilete + "_" + numarZbor + "_" + dataExport + "." + format;
                Path caleCompleta = folderExport.resolve(numeFisier);

                // Salvare fizică pe disc
                Files.write(caleCompleta, fisierGenerat);

                // 5. Raportăm succesul înapoi în firul grafic principal JavaFX
                Platform.runLater(() -> {
                    String mesajSuccesTemplate = bundle.getString("export.success.message");
                    String mesajFinal = mesajSuccesTemplate.contains("%s")
                            ? String.format(mesajSuccesTemplate, numeFisier)
                            : mesajSuccesTemplate + " " + numeFisier;

                    viewModel.mesajSuccesProperty().set(mesajFinal);
                    viewModel.mesajEroareProperty().set("");
                });

            } catch (Exception e) {
                // 6. Tratarea eșecurilor de rețea sau drepturi scriere I/O
                Platform.runLater(() -> {
                    String mesajEroareGeneric = bundle.getString("export.error.generic");
                    viewModel.mesajEroareProperty().set(mesajEroareGeneric + ": " + e.getMessage());
                    viewModel.mesajSuccesProperty().set("");
                });
            }
        });
    }
}