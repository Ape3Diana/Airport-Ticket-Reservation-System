package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.FlightSeatMapViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.ResourceBundle;

/**
 * Ecranul interactiv pentru vizualizarea matricei de locuri (Harta Scaunelor) dintr-un zbor.
 * Permite vânzarea directă a biletelor, actualizarea numelui pasagerului, anularea lor sau exportul PDF.
 */
public class FlightSeatMapView {

    // =========================================================================
    // 1. Componente FXML: Informații Globale și Container Matrice
    // =========================================================================

    @FXML private Label lblDetaliiZbor;
    @FXML private GridPane gridLocuri; // Containerul grid în care se generează programatic butoanele scaunelor

    // =========================================================================
    // 2. Componente FXML: Câmpuri Formular Scaun Selectat
    // =========================================================================

    @FXML private TextField txtLoc;
    @FXML private TextField txtNumePasager;
    @FXML private TextField txtPret;
    @FXML private TextField txtDataVanzare;
    @FXML private Label lblStatus; // Indicator verde pentru raportarea stării operațiunilor locale

    // =========================================================================
    // 3. Componente FXML: Butoane Acțiuni Bilet
    // =========================================================================

    @FXML private Button btnVanzare;
    @FXML private Button btnActualizare;
    @FXML private Button btnAnulare;
    @FXML private Button btnExportPDF;

    // =========================================================================
    // 4. Atribute, Callbacks și ViewModel
    // =========================================================================

    private FlightSeatMapViewModel viewModel;
    private Runnable onDataChangedCallback = () -> {}; // Rulat la modificări interne pentru a anunța ecranul părinte

    // =========================================================================
    // 5. Inițializare Date (Metodă apelată manual după încărcarea FXML)
    // =========================================================================

    /**
     * Configurează starea ecranului, asociază legăturile reactive și desenează scaunele.
     */
    public void initData(String idZborStr, String numarZbor, String plecare, String sosire, String oraDecolare, String oraAterizare, String pretStr, String locuriLibereStr) {
        this.viewModel = new FlightSeatMapViewModel();

        int idZbor = Integer.parseInt(idZborStr);
        double pret = Double.parseDouble(pretStr);
        int locuriLibereInitiale = Integer.parseInt(locuriLibereStr);

        // Mapare bidirecțională / simplă a datelor formularului de detalii loc
        txtNumePasager.textProperty().bindBidirectional(viewModel.numePasagerProperty());
        txtLoc.textProperty().bind(viewModel.numarLocProperty());
        txtDataVanzare.textProperty().bind(viewModel.dataVanzareProperty());

        lblStatus.textProperty().bind(viewModel.mesajStatusProperty());
        lblStatus.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Legare dinamică a stării de disponibilitate (Disabled) a butoanelor de acțiune
        btnVanzare.disableProperty().bind(viewModel.vanzareDisabledProperty());
        btnActualizare.disableProperty().bind(viewModel.actualizareDisabledProperty());
        btnAnulare.disableProperty().bind(viewModel.anulareDisabledProperty());
        btnExportPDF.disableProperty().bind(viewModel.exportPdfDisabledProperty());

        ResourceBundle bundle = LanguageManager.getBundle();
        lblDetaliiZbor.setText(bundle.getString("seatmap.flight.word") + " " + numarZbor + " | " + plecare + " -> " + sosire);
        txtPret.setText(pretStr);

        // Interceptarea erorilor asincrone pentru afișarea unui pop-up modal eroare
        viewModel.setOnShowErrorPopup(mesaj -> arataPopup(Alert.AlertType.ERROR, bundle.getString("popup.error.title"), mesaj));

        // Transmiterea detaliilor de business spre engine-ul din ViewModel
        viewModel.setDetaliiZbor(numarZbor, plecare, sosire, oraDecolare, oraAterizare);
        viewModel.initializeazaZbor(idZbor, pret);
        viewModel.calculeazaCapacitateReala(locuriLibereInitiale);

        // Primul desen al matricei fizice de locuri în avion
        deseneazaMatriceLocuri();

        // Ascultător pentru declanșarea re-desenării hărții din ViewModel (ex: după vânzare/anulare)
        viewModel.setOnRefreshMatrix(v -> {
            deseneazaMatriceLocuri();
            onDataChangedCallback.run(); // Notifică și ecranul principal (EmployeeFlightManagementView)
        });
    }

    // =========================================================================
    // 6. Logica de Redesenare Grafică (UI Generation)
    // =========================================================================

    /**
     * Construiește programatic grila de butoane pentru scaune (15 rânduri x 6 coloane).
     * Gestionează spațierea culoarului central din avion.
     */
    private void deseneazaMatriceLocuri() {
        gridLocuri.getChildren().clear();

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 6; col++) {
                char litera = (char) ('A' + col);
                String denumireLoc = (row + 1) + String.valueOf(litera);

                Button btnLoc = new Button(denumireLoc);
                btnLoc.setPrefSize(45, 45);

                // Legarea culorii fundalului și a blocării butonului de logica din ViewModel (Ocupat/Liber/Selectat)
                btnLoc.styleProperty().bind(viewModel.seatStyleProperty(denumireLoc));
                btnLoc.disableProperty().bind(viewModel.seatDisableProperty(denumireLoc));

                // Acțiune la click pe scaun
                btnLoc.setOnAction(e -> viewModel.selecteazaLoc(denumireLoc));

                // Adăugare în grid, lăsând un spațiu liber la mijloc (culoar) prin formula: col + (col / 3)
                gridLocuri.add(btnLoc, col + (col / 3), row);
            }
        }
    }

    // =========================================================================
    // 7. Gestionare Evenimente și Setters (Action Handlers)
    // =========================================================================

    public void setOnDataChangedCallback(Runnable callback) {
        this.onDataChangedCallback = callback;
    }

    @FXML private void onVindeBilet() { viewModel.getVindeBiletCommand().execute(); }
    @FXML private void onActualizeazaBilet() { viewModel.getActualizeazaBiletCommand().execute(); }
    @FXML private void onAnuleazaBilet() { viewModel.getAnuleazaBiletCommand().execute(); }
    @FXML private void onExportPDF() { viewModel.getExportPdfCommand().execute(); }

    private void arataPopup(Alert.AlertType tip, String titlu, String continut) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(continut);
        alert.showAndWait();
    }
}