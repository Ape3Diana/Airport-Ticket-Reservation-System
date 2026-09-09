package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.viewmodel.AdminDashboardViewModel;
import com.aeroport.viewmodel.commands.ICommand;

/**
 * Comandă simplă de rutare internă.
 * Schimbă contextul vizual curent al administratorului către ecranul public de căutare zboruri.
 */
public class NavZboruriCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final AdminDashboardViewModel viewModel;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public NavZboruriCommand(AdminDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // =========================================================================
    // 3. Execuție Rutare
    // =========================================================================

    @Override
    public void execute() {
        // Redirecționare către vizualizarea neautentificată/generală a tabelelor de zboruri
        viewModel.navigheaza("/fxml/UnauthenticatedFlightView.fxml");
    }
}