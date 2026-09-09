package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.AdminUserManagementViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlerul vizual pentru ecranul de administrare și gestiune a utilizatorilor sistemului.
 * Permite crearea, editarea, ștergerea, filtrarea și exportul utilizatorilor.
 */
public class AdminUserManagementView {

    // =========================================================================
    // 1. Componente FXML: Tabelul de Utilizatori
    // =========================================================================

    @FXML private TableView<List<String>> tblUtilizatori;
    @FXML private TableColumn<List<String>, String> colNume;
    @FXML private TableColumn<List<String>, String> colEmail;
    @FXML private TableColumn<List<String>, String> colTelefon;
    @FXML private TableColumn<List<String>, String> colRol;
    @FXML private TableColumn<List<String>, String> colParola;
    @FXML private TableColumn<List<String>, String> colDataCreare;

    // =========================================================================
    // 2. Componente FXML: Formularul de Introducere / Editare Date
    // =========================================================================

    @FXML private TextField txtNumeComplet;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtParola;
    @FXML private ComboBox<String> comboRol;        // Rolul selectat în formular pentru salvare
    @FXML private ComboBox<String> comboFiltruRol;  // Filtrul global de roluri de deasupra tabelului
    @FXML private Label lblStatus;                  // Etichetă inline pentru mesaje rapide de status

    // =========================================================================
    // 3. Atribute și ViewModel
    // =========================================================================

    private AdminUserManagementViewModel viewModel;

    // =========================================================================
    // 4. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        // Constructor gol conform bunelor practici MVVM - UI-ul nu se injectează în ViewModel
        viewModel = new AdminUserManagementViewModel();

        // Legături bidirecționale pentru câmpurile formularului (Sincronizare automată text <-> proprietate)
        txtNumeComplet.textProperty().bindBidirectional(viewModel.numeCompletProperty());
        txtEmail.textProperty().bindBidirectional(viewModel.emailProperty());
        txtTelefon.textProperty().bindBidirectional(viewModel.telefonProperty());
        txtParola.textProperty().bindBidirectional(viewModel.parolaProperty());
        comboRol.valueProperty().bindBidirectional(viewModel.rolSelectatProperty());
        comboFiltruRol.valueProperty().bindBidirectional(viewModel.rolFiltruSelectatProperty());

        // Legătură simplă pentru textul de status (Unidirecțional: ViewModel -> View)
        lblStatus.textProperty().bind(viewModel.statusProperty());

        // Popularea listelor derulante cu opțiunile din ViewModel
        comboRol.setItems(viewModel.getOptiuniRoluriFormular());
        comboFiltruRol.setItems(viewModel.getOptiuniRoluriFiltru());

        // Ascultător pentru schimbarea filtrului din combo - execută automat comanda de filtrare
        comboFiltruRol.setOnAction(event -> viewModel.getFilterCommand().execute());

        // Maparea datelor pe coloanele tabelului (Extragere elemente indexate din List<String>)
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(1)));
        colNume.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(2)));
        colRol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(3)));
        colTelefon.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(4)));
        colParola.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(5)));
        colDataCreare.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().get(6)));

        // Conectarea colecției observabile din ViewModel la tabelul grafic
        tblUtilizatori.setItems(viewModel.getListaUtilizatoriAfisare());

        // Legarea reactivă a selecției din tabel către proprietatea centrală din ViewModel
        viewModel.selectedUserProperty().bind(tblUtilizatori.getSelectionModel().selectedItemProperty());

        // Configurare Callbacks pentru afișarea ferestrelor modale (Pop-up) de succes și eroare
        ResourceBundle bundle = LanguageManager.getBundle();
        viewModel.setOnShowSuccessPopup(mesaj -> arataPopup(Alert.AlertType.INFORMATION, bundle.getString("popup.success.title"), mesaj));
        viewModel.setOnShowErrorPopup(mesaj -> arataPopup(Alert.AlertType.ERROR, bundle.getString("popup.error.title"), mesaj));

        // Încărcarea și filtrarea inițială a listei de utilizatori
        viewModel.getFilterCommand().execute();
    }

    // =========================================================================
    // 5. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    /**
     * Resetarea selecției din tabel și golirea completă a câmpurilor de text din formular.
     */
    @FXML
    private void onClearForm() {
        tblUtilizatori.getSelectionModel().clearSelection();
        viewModel.curataFormular();
    }

    /**
     * Declanșează comanda de export a tabelului curent în format CSV.
     */
    @FXML private void onExportCsv() { viewModel.getExportCommand().execute(); }

    /**
     * Declanșează comanda de salvare (adăugare sau actualizare) a utilizatorului curent.
     */
    @FXML private void onSave() { viewModel.getSaveCommand().execute(); }

    /**
     * Declanșează comanda de ștergere a utilizatorului selectat în tabel.
     */
    @FXML private void onDelete() { viewModel.getDeleteCommand().execute(); }

    // =========================================================================
    // 6. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    /**
     * Afișează o fereastră modală de avertizare sau informare (Alert).
     */
    private void arataPopup(Alert.AlertType tip, String titlu, String continut) {
        Alert alert = new Alert(tip);
        alert.setTitle(titlu);
        alert.setHeaderText(null);
        alert.setContentText(continut);
        alert.showAndWait();
    }
}