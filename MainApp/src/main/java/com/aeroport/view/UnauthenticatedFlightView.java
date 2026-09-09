package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.UnauthenticatedFlightViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

/**
 * Controlerul ecranului implicit de căutare pentru clienți / utilizatori neautentificați.
 * Oferă o vizualizare rapidă a zborurilor disponibile fără acces la acțiuni sensibile (vânzare/ștergere).
 */
public class UnauthenticatedFlightView {

    // =========================================================================
    // 1. Componente FXML: Formular Căutare Simplă
    // =========================================================================

    @FXML private TextField txtPlecare;
    @FXML private TextField txtSosire;
    @FXML private DatePicker dpData;
    @FXML private Label lblEroare; // Indicator inline pentru erori de completare

    // =========================================================================
    // 2. Componente FXML: Tabel Afișare Zboruri Publice
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
    // 3. Atribute și ViewModel
    // =========================================================================

    private UnauthenticatedFlightViewModel viewModel;

    // =========================================================================
    // 4. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        this.viewModel = new UnauthenticatedFlightViewModel();

        // Legături reactive bidirecționale pentru intrările utilizatorului anonim
        txtPlecare.textProperty().bindBidirectional(viewModel.plecareProperty());
        txtSosire.textProperty().bindBidirectional(viewModel.sosireProperty());
        dpData.valueProperty().bindBidirectional(viewModel.dataZborProperty());
        lblEroare.textProperty().bind(viewModel.mesajEroareProperty());

        // Configurarea defensivă a coloanelor tabelului cu verificări de dimensiune (Evitare IndexOutOfBoundsException)
        colNrZbor.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 0 ? d.getValue().get(0) : ""));
        colPlecare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 1 ? d.getValue().get(1) : ""));
        colSosire.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 2 ? d.getValue().get(2) : ""));
        colOraPlecare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 3 ? d.getValue().get(3) : ""));
        colOraSosire.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 4 ? d.getValue().get(4) : ""));
        colPret.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 5 ? d.getValue().get(5) : ""));
        colLocuri.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().size() > 6 ? d.getValue().get(6) : ""));

        // Atașare colecție observabilă la elementul tabelar grafic
        tblZboruri.setItems(viewModel.getListaZboruriAfisare());

        // Înregistrare pop-up eroare aliniat la stilul panourilor securizate ale aplicației
        viewModel.setOnShowErrorPopup(mesaj -> arataPopup(Alert.AlertType.ERROR,
                LanguageManager.getBundle().getString("popup.error.title"), mesaj));
    }

    // =========================================================================
    // 5. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML
    private void onCautaClick() {
        // Executarea comenzii de căutare expuse de ViewModel
        viewModel.getSearchFlightsCommand().execute();
    }

    // =========================================================================
    // 6. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    private void arataPopup(Alert.AlertType tip, String titlu, String continut) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(continut);
        alert.showAndWait();
    }
}