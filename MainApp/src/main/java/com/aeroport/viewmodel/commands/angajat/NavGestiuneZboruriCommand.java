package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.viewmodel.EmployeeDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă de rutare simplă în interiorul ecranului principal al angajatului.
 * Schimbă sub-ecranul curent cu panoul complex de gestiune zboruri, bilete și exporturi.
 */
public class NavGestiuneZboruriCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final EmployeeDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavGestiuneZboruriCommand(EmployeeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Redirecționare
    // =========================================================================

    @Override
    public void execute() {
        // Re-randează StackPane-ul principal cu interfața dedicată angajatului autentificat
        viewModel.navigheaza("/fxml/EmployeeFlightManagementView.fxml");
    }
}