package com.aeroport.view;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.EmployeeDashboardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.io.IOException;

/**
 * Controlerul vizual pentru panoul de control al angajaților (Employee Dashboard).
 * Permite comutarea între ferestrele de căutare simplă și gestiunea avansată a locurilor din zboruri.
 */
public class EmployeeDashboardView {

    // =========================================================================
    // 1. Componente FXML
    // =========================================================================

    @FXML
    private StackPane employeeContentArea; // Zona dinamică în care se înlocuiesc interfețele secundare

    // =========================================================================
    // 2. Atribute și ViewModel
    // =========================================================================

    private EmployeeDashboardViewModel viewModel;

    // =========================================================================
    // 3. Ciclu de Viață și Inițializare (Lifecycle)
    // =========================================================================

    @FXML
    public void initialize() {
        this.viewModel = new EmployeeDashboardViewModel();

        // Înregistrarea mecanismului de redirecționare internă
        viewModel.setActiuneNavigare(this::incarcaEcran);

        // Restaurarea ecranului salvat în sesiunea curentă a angajatului
        viewModel.incarcaPaginaSalvata();
    }

    // =========================================================================
    // 4. Gestionare Evenimente (Action Handlers)
    // =========================================================================

    /**
     * Comută ecranul central pe interfața de căutare rapidă/vizualizare simplă a zborurilor.
     */
    @FXML
    private void onNavCautaZboruri() {
        viewModel.getNavCautaZboruriCommand().execute();
    }

    /**
     * Comută ecranul central pe panoul complex de gestiune a biletelor și export.
     */
    @FXML
    private void onNavGestiune() {
        viewModel.getNavGestiuneZboruriCommand().execute();
    }

    // =========================================================================
    // 5. Metode Ajutătoare (Helper Methods)
    // =========================================================================

    /**
     * Rulează încărcarea unui fișier FXML și gestionarea limbilor pe nodul grafic principal.
     */
    private void incarcaEcran(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml), LanguageManager.getBundle());
            Node ecran = loader.load();
            employeeContentArea.getChildren().setAll(ecran);
        } catch (IOException e) {
            String msg = LanguageManager.getBundle().getString("error.load_screen");
            System.err.println(msg + " " + rutaFxml);
            e.printStackTrace();
        }
    }
}