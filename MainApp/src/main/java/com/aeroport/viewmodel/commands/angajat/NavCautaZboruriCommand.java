package com.aeroport.viewmodel.commands.angajat;

import com.aeroport.viewmodel.EmployeeDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă de rutare simplă în interiorul ecranului principal al angajatului.
 * Încarcă vizualizarea generală/publică de căutare zboruri în panoul central.
 */
public class NavCautaZboruriCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final EmployeeDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavCautaZboruriCommand(EmployeeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Redirecționare
    // =========================================================================

    @Override
    public void execute() {
        // Schimbă nodul grafic prin intermediul mecanismului central de navigare
        viewModel.navigheaza("/fxml/UnauthenticatedFlightView.fxml");
    }
}