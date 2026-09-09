package com.aeroport.view;

import com.aeroport.viewmodel.MainContainerViewModel;
import com.aeroport.viewmodel.utils.LanguageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Fereastra principală ancoră a întregului sistem (Shell / Root Window).
 * Conține bara de sus pentru autentificare (Login/Logout), selectorul de limbă,
 * sistemul global de primire a notificărilor asincrone de securitate și zona principală de conținut.
 */
public class MainContainerView {

    // =========================================================================
    // 1. Componente FXML: Elemente de Text Static / Dinamic (I18N)
    // =========================================================================

    @FXML private Label lblEmail;
    @FXML private Label lblParola;
    @FXML private Label lblLimba;
    @FXML private Label lblUserInfo; // Text informativ despre utilizatorul logat (Nume, Rol)
    @FXML private Label statusLabel;  // Etichetă erori rapide de rețea/autentificare

    // =========================================================================
    // 2. Componente FXML: Câmpuri Introducere Date și Acțiuni
    // =========================================================================

    @FXML private TextField emailField;
    @FXML private PasswordField parolaField;
    @FXML private Button btnLogin;
    @FXML private Button btnLogout;
    @FXML private ComboBox<String> langCombo;

    // =========================================================================
    // 3. Componente FXML: Layout-uri Continentale
    // =========================================================================

    @FXML private StackPane contentArea; // Zona principală unde se încarcă ecranele (Unauth / Admin / Employee / Manager)
    @FXML private HBox loginBox;         // Container vizibil doar când utilizatorul este anonim
    @FXML private HBox userBox;          // Container vizibil doar când utilizatorul este autentificat

    // =========================================================================
    // 4. Atribute și ViewModel
    // =========================================================================

    private MainContainerViewModel viewModel;

    // =========================================================================
    // 5. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        this.viewModel = new MainContainerViewModel();

        // Bindings bidirecționale și simple pentru date brute
        emailField.textProperty().bindBidirectional(viewModel.emailProperty());
        parolaField.textProperty().bindBidirectional(viewModel.parolaProperty());
        statusLabel.textProperty().bind(viewModel.mesajStatusProperty());
        lblUserInfo.textProperty().bind(viewModel.infoUtilizatorProperty());
        langCombo.valueProperty().bindBidirectional(viewModel.limbaSelectataProperty());

        // Legarea textelor etichetelor statice direct de proprietățile internaționalizate din ViewModel
        lblEmail.textProperty().bind(viewModel.labelEmailTextProperty());
        lblParola.textProperty().bind(viewModel.labelParolaTextProperty());
        btnLogin.textProperty().bind(viewModel.buttonLoginTextProperty());
        lblLimba.textProperty().bind(viewModel.labelLimbaTextProperty());
        btnLogout.textProperty().bind(viewModel.buttonLogoutTextProperty());

        // Gestiunea vizibilității componentelor bazată pe starea de logare din ViewModel
        loginBox.visibleProperty().bind(viewModel.autentificatProperty().not());
        loginBox.managedProperty().bind(viewModel.autentificatProperty().not());
        userBox.visibleProperty().bind(viewModel.autentificatProperty());
        userBox.managedProperty().bind(viewModel.autentificatProperty());

        // Adăugare limbi disponibile în selector
        langCombo.getItems().addAll("Română", "English", "Deutsch");

        // Setare strategii de Callbacks (Schimbare ecran și alerte eroare)
        viewModel.setActiuneSchimbaEcran(this::incarcaEcran);
        viewModel.setOnShowError(mesaj -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(LanguageManager.getBundle().getString("popup.error.title"));
            alert.setHeaderText(null);
            alert.setContentText(mesaj);
            alert.showAndWait();
        });

        // Configurare Interceptare Notificări Asincrone (Sistemul Push prin Server-Sent Events / Polling)
        viewModel.setOnShowNotifications(notificari -> {
            for (java.util.Map<String, Object> notificare : notificari) {
                String canal = (String) notificare.get("tipCanal");
                String mesaj = (String) notificare.get("mesaj");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                // Permite utilizatorului să interacționeze simultan cu restul UI-ului fără a bloca aplicația
                alert.initModality(javafx.stage.Modality.NONE);

                alert.setTitle("Notificare de Securitate");
                alert.setHeaderText("Mesaj nou recepționat prin " + canal);
                alert.setContentText(mesaj);
                alert.show();
            }
        });

        // Încărcarea ecranului implicit inițial (vizualizarea zborurilor fără cont)
        viewModel.incarcaEcranInitial();
    }

    // =========================================================================
    // 6. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML private void handleLogin() { viewModel.getLoginCommand().execute(); }
    @FXML private void handleLogout() { viewModel.getLogoutCommand().execute(); }

    @FXML
    private void handleLanguageChange() {
        // Zero logică în View. Comanda din ViewModel se ocupă de reconfigurarea resurselor lingvistice
        viewModel.getChangeLanguageCommand().execute();
    }

    // =========================================================================
    // 7. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    private void incarcaEcran(String rutaFxml) {
        try {
            ResourceBundle bundle = LanguageManager.getBundle();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml), bundle);
            Node ecran = loader.load();
            contentArea.getChildren().setAll(ecran);
        } catch (IOException e) {
            System.err.println("Eroare FXML: " + rutaFxml);
        }
    }
}