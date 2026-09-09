package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.FlightSeatMapViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

/**
 * Comandă asincronă responsabilă de exportul unui singur bilet în format PDF.
 * Trimite metadatele biletului către microserviciul de raportare și salvează PDF-ul local.
 */
public class ExportBiletPdfCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final FlightSeatMapViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public ExportBiletPdfCommand(FlightSeatMapViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Asincronă
    // =========================================================================

    @Override
    public void execute() {
        int idBilet = viewModel.getIdBiletCurent();
        String numarLoc = viewModel.numarLocProperty().get();
        String numePasager = viewModel.numePasagerProperty().get();
        String dataAchizitie = viewModel.dataVanzareProperty().get();

        // Validare defensivă: Dacă nu avem un bilet valid selectat sau locul e gol, oprim execuția
        if (idBilet == -1 || numarLoc.isEmpty()) return;

        ResourceBundle bundle = LanguageManager.getBundle();

        // Rulăm procesarea binară pe un fir de execuție secundar pentru a menține UI-ul responsiv
        CompletableFuture.runAsync(() -> {
            try {
                // Maparea structurii rândului 1 de informații (Date de zbor și loc)
                Map<String, Object> randul1 = new LinkedHashMap<>();
                randul1.put("Identificare", "Zbor: " + viewModel.getNumarZborInfo());
                randul1.put("Aeroport", "Plec: " + viewModel.getPlecareInfo());
                randul1.put("Data / Ora", "Dec: " + viewModel.getOraDecolareInfo());
                randul1.put("Rezervare", "Loc: " + numarLoc);
                randul1.put("Achiziție", "Data: " + (dataAchizitie != null && !dataAchizitie.isEmpty() ? dataAchizitie : "-"));

                // Maparea structurii rândului 2 de informații (Date pasager și preț)
                Map<String, Object> randul2 = new LinkedHashMap<>();
                randul2.put("Identificare", "Pasager: " + (numePasager != null && !numePasager.isEmpty() ? numePasager : "-"));
                randul2.put("Aeroport", "Sos: " + viewModel.getSosireInfo());
                randul2.put("Data / Ora", "Ater: " + viewModel.getOraAterizareInfo());
                randul2.put("Rezervare", "Preț: " + viewModel.getPretCurent() + " EUR");
                randul2.put("Achiziție", "-");

                // Solicitarea generării PDF-ului către microserviciul de export
                String exportUrl = restClient.getUrlExport() + "/pdf";
                byte[] fisierGenerat = restClient.downloadBinaryData(exportUrl, List.of(randul1, randul2));

                // Crearea folderului local pentru stocarea biletelor angajaților
                Path folderExport = Paths.get("src/main/resources/employee_exports");
                Files.createDirectories(folderExport);

                String cuvantBilet = bundle.getString("export.filename.single_ticket");

                // Generarea unui timestamp unic pentru a evita blocajele de suprascriere I/O
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
                String timestamp = LocalDateTime.now().format(dtf);

                String numeFisier = cuvantBilet + "_" + idBilet + "_" + timestamp + ".pdf";
                Path caleCompleta = folderExport.resolve(numeFisier);

                // Scrierea fișierului binar fizic pe disc
                Files.write(caleCompleta, fisierGenerat);

                // Actualizăm statusul cu textul verde în interfața grafică pe firul principal
                Platform.runLater(() -> {
                    String msg = bundle.getString("export.success.message");
                    viewModel.mesajStatusProperty().set(msg.replace("%s", numeFisier));
                });

            } catch (Exception e) {
                // În caz de eroare la I/O sau rețea, trimitem o alertă de tip pop-up prin ViewModel
                Platform.runLater(() -> {
                    viewModel.triggerError(bundle.getString("export.error.generic") + ": " + e.getMessage());
                    viewModel.mesajStatusProperty().set(""); // Se golește eticheta verde de status inline
                });
            }
        });
    }
}