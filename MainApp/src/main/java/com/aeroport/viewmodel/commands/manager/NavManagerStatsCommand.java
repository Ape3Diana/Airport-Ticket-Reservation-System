package com.aeroport.viewmodel.commands.manager;

import com.aeroport.viewmodel.ManagerDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă simplă de rutare internă utilizată de panoul Managerului.
 * Redirecționează zona centrală spre vizualizarea rapoartelor și graficelor statistice.
 */
public class NavManagerStatsCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavManagerStatsCommand(ManagerDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Navigarea
    // =========================================================================

    @Override
    public void execute() {
        // Solicită re-încărcarea StackPane-ului central utilizând calea către ecranul de statistici
        viewModel.navigheaza("/fxml/ManagerStatsView.fxml");
    }
}