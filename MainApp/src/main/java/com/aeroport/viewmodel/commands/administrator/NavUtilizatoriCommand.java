package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.viewmodel.AdminDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă simplă de rutare internă.
 * Instruiește zona centrală a panoului Admin să încarce sub-ecranul de gestiune a utilizatorilor.
 */
public class NavUtilizatoriCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    /** ViewModel-ul Dashboard părinte care deține controlul asupra callback-ului de schimbare a scenelor. */
    private final AdminDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavUtilizatoriCommand(AdminDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Rutare
    // =========================================================================

    @Override
    public void execute() {
        // Solicită re-redesenarea StackPane-ului central utilizând calea relativă către FXML
        viewModel.navigheaza("/fxml/AdminUserManagementView.fxml");
    }
}