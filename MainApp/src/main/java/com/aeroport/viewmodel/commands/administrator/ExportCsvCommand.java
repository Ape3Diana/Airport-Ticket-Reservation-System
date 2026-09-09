package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.AdminUserManagementViewModel;
import com.aeroport.viewmodel.utils.LanguageManager;
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
 * Comandă asincronă utilizată pentru exportul listei curente de utilizatori în format CSV.
 * Datele sunt preluate din tabel, mapate ordonat, trimise la microserviciul de export și salvate local pe disc.
 */
public class ExportCsvCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final AdminUserManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public ExportCsvCommand(AdminUserManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Asincronă
    // =========================================================================

    /**
     * Porneste execuția exportului pe un fir de execuție secundar (CompletableFuture)
     * pentru a menține interfața JavaFX complet fluidă și responsivă în timpul descărcării.
     */
    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();

        // Rulăm complet asincron procesarea fișierelor și apelul REST
        CompletableFuture.runAsync(() -> {
            try {
                // 1. Preluăm lista exact așa cum este filtrată și afișată pe ecran în acest moment
                List<List<String>> utilizatoriTabel = viewModel.getListaUtilizatoriAfisare();

                if (utilizatoriTabel.isEmpty()) {
                    Platform.runLater(() -> viewModel.triggerError(bundle.getString("admin.export.error.no_users")));
                    return;
                }

                // 2. Mapăm datele pentru microserviciu folosind LinkedHashMap pentru a păstra ordinea coloanelor intactă
                List<Map<String, Object>> dateExport = new ArrayList<>();
                for (List<String> rand : utilizatoriTabel) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("ID", rand.get(0));
                    map.put("Email", rand.get(1));
                    map.put("Nume Complet", rand.get(2));
                    map.put("Rol", rand.get(3));
                    map.put("Telefon", rand.get(4));
                    map.put("Parola", rand.get(5));
                    map.put("Data Creării", rand.get(6));
                    dateExport.add(map);
                }

                // 3. Apelăm microserviciul dedicat prin gateway (solicitând mod-ul binar /csv)
                String exportUrl = restClient.getUrlExport() + "/csv";
                byte[] fisierGenerat = restClient.downloadBinaryData(exportUrl, dateExport);

                // 4. Pregătim structura fizică de directoare în resursele proiectului
                String directoryPath = "src/main/resources/admin_exports";
                Path folderExport = Paths.get(directoryPath);
                Files.createDirectories(folderExport);

                // 5. Construim numele fișierului dinamic pe baza filtrului lingvistic aplicat (ex: Toți / Angajat / Manager)
                String prefix = bundle.getString("admin.export.filename.prefix");
                String filtruSelectat = viewModel.rolFiltruSelectatProperty().get();
                String filtruPentruNume = (filtruSelectat == null || filtruSelectat.isEmpty())
                        ? bundle.getString("admin.filter.all")
                        : filtruSelectat;

                // Formatare denumire: eliminare spații și aplicare timestamp unic (prevenire blocaje I/O Windows)
                filtruPentruNume = filtruPentruNume.replace(" ", "_");
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

                String fileName = prefix + "_" + filtruPentruNume + "_" + timestamp + ".csv";
                Path fullPath = folderExport.resolve(fileName);

                // 6. Scriem matricea de octeți (fișierul descarcat) direct pe disc
                Files.write(fullPath, fisierGenerat);

                // 7. Notificăm cu succes utilizatorul pe firul principal de execuție UI
                Platform.runLater(() -> viewModel.triggerSuccess(bundle.getString("admin.export.success") + " " + fileName));

            } catch (Exception e) {
                // Prindere defensivă a erorilor de Input/Output sau rețea și trimitere pe firul UI
                Platform.runLater(() -> viewModel.triggerError(bundle.getString("admin.export.error") + " " + e.getMessage()));
            }
        });
    }
}