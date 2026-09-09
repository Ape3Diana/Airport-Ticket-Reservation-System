package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.EmployeeFlightManagementViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.List;
import javafx.application.Platform;

/**
 * Ecranul principal de lucru al angajatului pentru monitorizarea zborurilor,
 * selectarea lor și inițierea acțiunilor de export sau configurare a hărții locurilor.
 */
public class EmployeeFlightManagementView {

    // =========================================================================
    // 1. Componente FXML: Elemente de Căutare și Mesaje
    // =========================================================================

    @FXML private TextField txtCautare;
    @FXML private Button btnVizualizeaza;
    @FXML private Label lblEroare; // Folosită dinamic pentru mesaje de succes/eroare formatate

    // =========================================================================
    // 2. Componente FXML: Tabelul de Zboruri
    // =========================================================================

    @FXML private TableView<List<String>> tblZboruri;
    @FXML private TableColumn<List<String>, String> colId;
    @FXML private TableColumn<List<String>, String> colNumar;
    @FXML private TableColumn<List<String>, String> colPlecare;
    @FXML private TableColumn<List<String>, String> colSosire;
    @FXML private TableColumn<List<String>, String> colOraDecolare;
    @FXML private TableColumn<List<String>, String> colOraAterizare;
    @FXML private TableColumn<List<String>, String> colPret;
    @FXML private TableColumn<List<String>, String> colLocuri;

    // =========================================================================
    // 3. Componente FXML: Export Ticket System
    // =========================================================================

    @FXML private ComboBox<String> cmbExportFormat;
    @FXML private Button btnExport;

    // =========================================================================
    // 4. Atribute și ViewModel
    // =========================================================================

    private EmployeeFlightManagementViewModel viewModel;

    // =========================================================================
    // 5. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        viewModel = new EmployeeFlightManagementViewModel();

        // Bindings text și colecție date
        txtCautare.textProperty().bindBidirectional(viewModel.textCautareProperty());
        tblZboruri.setItems(viewModel.getListaZboruri());

        // Legare stilizată pentru mesaje și etichete verzi de informare directă
        lblEroare.textProperty().bind(viewModel.mesajSuccesProperty());
        lblEroare.visibleProperty().bind(viewModel.mesajSuccesProperty().isNotEmpty());
        lblEroare.managedProperty().bind(viewModel.mesajSuccesProperty().isNotEmpty());
        lblEroare.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Mapare celule tabel
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colNumar.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colPlecare.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colSosire.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colOraDecolare.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        colOraAterizare.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));
        colPret.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(6)));
        colLocuri.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(7)));

        // Formate export combobox setup
        cmbExportFormat.setItems(viewModel.getFormateExport());
        cmbExportFormat.valueProperty().bindBidirectional(viewModel.formatExportProperty());
        cmbExportFormat.getSelectionModel().select("csv");

        // Înregistrarea reacției de selecție din tabel
        viewModel.zborSelectatProperty().bind(tblZboruri.getSelectionModel().selectedItemProperty());

        // Controlul proprietății de activare a butoanelor bazat pe starea selecției din ViewModel
        btnExport.disableProperty().bind(viewModel.exportDisabledProperty());
        btnVizualizeaza.disableProperty().bind(viewModel.exportDisabledProperty());

        // Tratarea erorilor asincrone cu afișare modală securizată pe firul principal JavaFX
        viewModel.setErrorPopupAction(mesaj -> Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(LanguageManager.getBundle().getString("error.title.popup"));
            alert.setHeaderText(null);
            alert.setContentText(mesaj);
            alert.showAndWait();
            viewModel.clearError();
        }));

        // Încărcarea automată a tuturor zborurilor din sistem la pornire
        viewModel.getLoadAllFlightsEmployeeCommand().execute();
    }

    // =========================================================================
    // 6. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML
    private void onCautaClick() {
        viewModel.getSearchFlightsEmployeeCommand().execute();
    }

    @FXML
    private void onArataToateClick() {
        viewModel.textCautareProperty().set("");
        viewModel.getLoadAllFlightsEmployeeCommand().execute();
    }

    @FXML
    private void onVizualizeazaClick() {
        deschideFereastraLocuri(viewModel.zborSelectatProperty().get());
    }

    @FXML
    private void onExportClick() {
        viewModel.getExportTicketsEmployeeCommand().execute();
    }

    // =========================================================================
    // 7. Deschidere Sub-Ferestre (Navigation / Stage Control)
    // =========================================================================

    /**
     * Deschide într-o fereastră separată (Stage modal) harta interactivă a locurilor din zborul selectat.
     */
    private void deschideFereastraLocuri(List<String> zbor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FlightSeatMapView.fxml"));
            loader.setResources(LanguageManager.getBundle());
            Parent root = loader.load();

            FlightSeatMapView controller = loader.getController();
            controller.initData(
                    zbor.get(0), zbor.get(1), zbor.get(2), zbor.get(3),
                    zbor.get(4), zbor.get(5), zbor.get(6), zbor.get(7)
            );

            // Callback primit de la harta scaunelor pentru împrospătarea listei de zboruri când se vinde un bilet
            controller.setOnDataChangedCallback(() -> viewModel.getLoadAllFlightsEmployeeCommand().execute());

            Stage stage = new Stage();
            stage.setTitle(LanguageManager.getBundle().getString("seatmap.window.title") + " " + zbor.get(1));
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}