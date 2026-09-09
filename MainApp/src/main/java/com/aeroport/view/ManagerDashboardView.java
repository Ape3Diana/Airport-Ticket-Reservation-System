package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerDashboardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

/**
 * Controlerul vizual principal pentru interfața Managerului (Dashboard Manager).
 * Oferă butoanele de comutare spre fluxul de administrare zboruri sau ecranul grafic de statistici.
 */
public class ManagerDashboardView {

    // =========================================================================
    // 1. Componente FXML
    // =========================================================================

    @FXML
    private StackPane managerContentArea; // Zona centrală de comutare a panourilor managerului

    // =========================================================================
    // 2. Atribute și ViewModel
    // =========================================================================

    private ManagerDashboardViewModel viewModel;

    // =========================================================================
    // 3. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        this.viewModel = new ManagerDashboardViewModel();

        // Configurarea callback-ului de navigare: când ViewModel-ul cere o rută, View-ul o încarcă fizic
        viewModel.setActiuneNavigare(this::incarcaEcran);

        // Încărcarea paginii implicite salvate în variabila statică de stare a sesiunii
        viewModel.incarcaPaginaSalvata();
    }

    // =========================================================================
    // 4. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    @FXML
    private void onNavZboruri() {
        viewModel.getNavZboruriCommand().execute();
    }

    @FXML
    private void onNavStatistici() {
        viewModel.getNavStatisticiCommand().execute();
    }

    // =========================================================================
    // 5. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    private void incarcaEcran(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml), LanguageManager.getBundle());
            Node ecran = loader.load();
            managerContentArea.getChildren().setAll(ecran);
        } catch (IOException e) {
            System.err.println("Eroare la încărcarea ecranului managerului: " + rutaFxml);
            e.printStackTrace();
        }
    }
}