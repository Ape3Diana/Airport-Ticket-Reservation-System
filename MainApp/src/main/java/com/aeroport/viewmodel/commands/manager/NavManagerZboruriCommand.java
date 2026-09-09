package com.aeroport.viewmodel.commands.manager;

import com.aeroport.viewmodel.ManagerDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă simplă de rutare internă utilizată de panoul Managerului.
 * Redirecționează zona centrală din dashboard către panoul de operațiuni CRUD zboruri.
 */
public class NavManagerZboruriCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavManagerZboruriCommand(ManagerDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Navigare
    // =========================================================================

    @Override
    public void execute() {
        // Solicită re-încărcarea nodului grafic către FXML-ul unificat de management
        viewModel.navigheaza("/fxml/ManagerFlightManagementView.fxml");
    }
}