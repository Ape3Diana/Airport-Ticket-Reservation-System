package com.aeroport.view;

import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.utils.LanguageManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

/**
 * Panoul de management avansat al zborurilor, accesibil managerilor.
 * Permite operațiuni CRUD (Creare, Citire, Actualizare, Ștergere), filtre multiple simultane și export de date.
 */
public class ManagerFlightManagementView {

    // =========================================================================
    // 1. Componente FXML: Tabel Date Zboruri
    // =========================================================================

    @FXML private TableView<List<String>> tblZboruri;
    @FXML private TableColumn<List<String>, String> colNrZbor;
    @FXML private TableColumn<List<String>, String> colPlecare;
    @FXML private TableColumn<List<String>, String> colSosire;
    @FXML private TableColumn<List<String>, String> colOraPlecare;
    @FXML private TableColumn<List<String>, String> colOraSosire;
    @FXML private TableColumn<List<String>, String> colPret;
    @FXML private TableColumn<List<String>, String> colLocuri;

    // =========================================================================
    // 2. Componente FXML: Câmpuri Formular Introducere/Salvare
    // =========================================================================

    @FXML private TextField txtNumarZbor;
    @FXML private DatePicker dpDataZbor;
    @FXML private TextField txtOraPlecare;
    @FXML private TextField txtOraSosire;
    @FXML private TextField txtPret;
    @FXML private TextField txtLocuri;
    @FXML private ComboBox<String> comboPlecare;
    @FXML private ComboBox<String> comboSosire;

    // =========================================================================
    // 3. Componente FXML: Câmpuri Căutare și Filtre Curente
    // =========================================================================

    @FXML private TextField txtCautaPlecare;
    @FXML private TextField txtCautaSosire;
    @FXML private TextField txtCautaNumar;
    @FXML private DatePicker dpCautaData;

    // =========================================================================
    // 4. Componente FXML: Format Export și Feedback
    // =========================================================================

    @FXML private ComboBox<String> cmbExportFormat;
    @FXML private Label lblStatus;

    // =========================================================================
    // 5. Atribute și ViewModel
    // =========================================================================

    private ManagerFlightManagementViewModel viewModel;

    // =========================================================================
    // 6. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        viewModel = new ManagerFlightManagementViewModel();

        // Callback pentru avertismente/erori de completare greșită a formatelor de dată sau preț
        viewModel.setOnShowErrorPopup(mesaj -> arataAlert(Alert.AlertType.ERROR, LanguageManager.getBundle().getString("popup.error.title"), mesaj));

        // Sincronizare câmpuri formular de editare/salvare zbor
        txtNumarZbor.textProperty().bindBidirectional(viewModel.numarZborProperty());
        dpDataZbor.valueProperty().bindBidirectional(viewModel.dataZborProperty());
        txtOraPlecare.textProperty().bindBidirectional(viewModel.oraPlecareProperty());
        txtOraSosire.textProperty().bindBidirectional(viewModel.oraSosireProperty());
        txtPret.textProperty().bindBidirectional(viewModel.pretProperty());
        txtLocuri.textProperty().bindBidirectional(viewModel.locuriProperty());

        // Configurarea și legarea listelor aeroporturilor din sistem în ComboBox-uri
        comboPlecare.setItems(viewModel.getListaNumeAeroporturi());
        comboSosire.setItems(viewModel.getListaNumeAeroporturi());
        comboPlecare.valueProperty().bindBidirectional(viewModel.plecareProperty());
        comboSosire.valueProperty().bindBidirectional(viewModel.sosireProperty());

        // Sincronizare câmpuri de căutare / filtrare dinamică
        txtCautaPlecare.textProperty().bindBidirectional(viewModel.plecareCautaProperty());
        txtCautaSosire.textProperty().bindBidirectional(viewModel.sosireCautaProperty());
        dpCautaData.valueProperty().bindBidirectional(viewModel.dataCautaProperty());
        txtCautaNumar.textProperty().bindBidirectional(viewModel.cautaDupaNumarProperty());

        // Format selector pentru export
        cmbExportFormat.setItems(javafx.collections.FXCollections.observableArrayList("csv", "json", "xml", "doc"));
        cmbExportFormat.valueProperty().bindBidirectional(viewModel.formatExportProperty());

        // Legare etichetă status inline
        lblStatus.textProperty().bind(viewModel.statusProperty());

        // Mapare coloane tabel zboruri manager
        colNrZbor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(1)));
        colPlecare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(2)));
        colSosire.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(3)));
        colOraPlecare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(4)));
        colOraSosire.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(5)));
        colPret.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(6)));
        colLocuri.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(7)));

        tblZboruri.setItems(viewModel.getListaZboruriAfisare());

        // Legătură directă reactivă cu proprietatea din ViewModel (Fără if-uri)
        viewModel.selectedFlightProperty().bind(tblZboruri.getSelectionModel().selectedItemProperty());

        // Încărcare inițială date de la microserviciu
        viewModel.getLoadAllFlightsManagerCommand().execute();
    }

    // =========================================================================
    // 7. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML
    private void onArataToateZborurile() {
        tblZboruri.getSelectionModel().clearSelection();
        viewModel.curataToateFiltrele();
        viewModel.getLoadAllFlightsManagerCommand().execute();
    }

    @FXML private void onCautaZboruri() { viewModel.getSearchFlightsManagerCommand().execute(); }
    @FXML private void onFiltreazaNumar() { viewModel.getFilterByFlightNumberManagerCommand().execute(); }
    @FXML private void onSave() { viewModel.getSaveCommand().execute(); }
    @FXML private void onDelete() { viewModel.getDeleteCommand().execute(); }

    @FXML
    private void onClearForm() {
        tblZboruri.getSelectionModel().clearSelection();
        viewModel.curataFormular();
    }

    @FXML
    private void onExportData() {
        viewModel.getExportFlightsCommand().execute();
    }

    // =========================================================================
    // 8. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    private void arataAlert(Alert.AlertType tip, String titlu, String continut) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(continut);
        alert.showAndWait();
    }
}