package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerStatsViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Fereastra grafică de monitorizare a statisticilor de business dedicate managementului.
 * Randează grafice de tip BarChart (Venituri, Grad de Ocupare) și PieChart (Destinații populare).
 */
public class ManagerStatsView {

    // =========================================================================
    // 1. Componente FXML: Obiecte Grafice JavaFX (Charts)
    // =========================================================================

    @FXML private BarChart<String, Number> chartVenituri;
    @FXML private BarChart<String, Number> chartOcupare;
    @FXML private PieChart chartDestinatii;

    // =========================================================================
    // 2. Atribute și ViewModel
    // =========================================================================

    private ManagerStatsViewModel viewModel;

    // =========================================================================
    // 3. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        this.viewModel = new ManagerStatsViewModel();

        ResourceBundle bundle = LanguageManager.getBundle();
        viewModel.setOnShowErrorPopup(mesaj -> Platform.runLater(() -> arataAlertEroare(mesaj)));

        // Înregistrează evenimentul de împrospătare a graficelor când datele s-au descărcat cu succes
        viewModel.setOnDataLoadedCallback(this::populeazaGrafice);

        // Încărcarea datelor analitice inițiale
        viewModel.getLoadStatsCommand().execute();
    }

    // =========================================================================
    // 4. Logica de Redesenare/Populare Grafice (Data Visualization)
    // =========================================================================

    /**
     * Mapul brut extras din ViewModel este transformat în serii fizice de date (XYChart.Data și PieChart.Data)
     * și injectat asincron pe thread-ul de UI.
     */
    private void populeazaGrafice() {
        Platform.runLater(() -> {
            ResourceBundle bundle = LanguageManager.getBundle();

            // 1. Populare Grafic Venituri Lunare (BarChart)
            XYChart.Series<String, Number> seriesVenituri = new XYChart.Series<>();
            seriesVenituri.setName(bundle.getString("manager.stats.legend.revenue"));
            for (Map<String, Object> item : viewModel.getDataVenituri()) {
                seriesVenituri.getData().add(new XYChart.Data<>(
                        (String) item.get("eticheta"),
                        (Number) item.get("valoare")
                ));
            }
            chartVenituri.getData().setAll(seriesVenituri);

            // 2. Populare Grafic Procent Ocupare per Rută (BarChart)
            XYChart.Series<String, Number> seriesOcupare = new XYChart.Series<>();
            seriesOcupare.setName(bundle.getString("manager.stats.legend.occupancy"));
            for (Map<String, Object> item : viewModel.getDataOcupare()) {
                seriesOcupare.getData().add(new XYChart.Data<>(
                        (String) item.get("eticheta"),
                        (Number) item.get("valoare")
                ));
            }
            chartOcupare.getData().setAll(seriesOcupare);

            // 3. Populare Grafic Distribuție Destinații (PieChart)
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (Map<String, Object> item : viewModel.getDataDestinatii()) {
                pieData.add(new PieChart.Data(
                        (String) item.get("eticheta"),
                        ((Number) item.get("valoare")).doubleValue()
                ));
            }
            chartDestinatii.setData(pieData);
        });
    }

    // =========================================================================
    // 5. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML
    private void onRefreshStats() {
        viewModel.getLoadStatsCommand().execute();
    }

    // =========================================================================
    // 6. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    private void arataAlertEroare(String continut) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageManager.getBundle().getString("popup.error.title"));
        alert.setHeaderText(null);
        alert.setContentText(continut);
        alert.showAndWait();
    }
}