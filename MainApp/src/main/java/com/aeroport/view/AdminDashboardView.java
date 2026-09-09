package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.AdminDashboardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

/**
 * Controlerul vizual pentru panoul de administrare general (Dashboard Admin).
 * Gestionează navigarea principală între ecranele dedicate administratorului.
 */
public class AdminDashboardView {

    // =========================================================================
    // 1. Componente FXML (Elemente de Interfață Grafică)
    // =========================================================================

    @FXML
    private StackPane adminContentArea; // Zona centrală interschimbabilă unde se încarcă sub-ecranele

    // =========================================================================
    // 2. Atribute de Stare și ViewModel
    // =========================================================================

    private AdminDashboardViewModel viewModel; // ViewModel-ul asociat acestui ecran

    // =========================================================================
    // 3. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        // Inițializarea ViewModel-ului dedicat
        this.viewModel = new AdminDashboardViewModel();

        // Configurarea acțiunii de navigare prin intermediul unei referințe la metodă (Callback)
        viewModel.setActiuneNavigare(this::incarcaEcran);

        // Încărcarea automată a ultimului ecran salvat în starea aplicației
        viewModel.incarcaPaginaSalvata();
    }

    // =========================================================================
    // 4. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    /**
     * Declanșat la apăsarea butonului de navigare către Gestiune Zboruri.
     */
    @FXML
    private void onNavZboruri() {
        viewModel.getNavZboruriCommand().execute();
    }

    /**
     * Declanșat la apăsarea butonului de navigare către Gestiune Utilizatori.
     */
    @FXML
    private void onNavUtilizatori() {
        viewModel.getNavUtilizatoriCommand().execute();
    }

    // =========================================================================
    // 5. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    /**
     * Încarcă un fișier FXML în mod dinamic în zona centrală a panoului de administrare.
     * * @param rutaFxml Calea către fișierul XML care definește interfața grafică secundară.
     */
    private void incarcaEcran(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml), LanguageManager.getBundle());
            Node ecran = loader.load();
            adminContentArea.getChildren().setAll(ecran);
        } catch (IOException e) {
            System.err.println("Eroare la încărcarea ecranului: " + rutaFxml);
            e.printStackTrace();
        }
    }
}