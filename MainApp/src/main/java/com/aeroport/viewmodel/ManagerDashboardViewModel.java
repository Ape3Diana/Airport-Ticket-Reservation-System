package com.aeroport.viewmodel;

import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.manager.NavManagerZboruriCommand;
import com.aeroport.viewmodel.commands.manager.NavManagerStatsCommand;
import java.util.function.Consumer;

public class ManagerDashboardViewModel {

    // 1. Servicii / Utilitare
    // (Nu sunt necesare servicii de rețea în acest Dashboard)

    // 2. Proprietăți de Stare / Variabile statice
    private static String ultimaRuta = "/fxml/ManagerFlightManagementView.fxml";

    // 3. Comenzi (ICommand)
    private final ICommand navZboruriCommand;
    private final ICommand navStatisticiCommand;

    // 4. Callbacks
    private Consumer<String> actiuneNavigare;

    // 5. Constructor
    public ManagerDashboardViewModel() {
        this.navZboruriCommand = new NavManagerZboruriCommand(this);
        this.navStatisticiCommand = new NavManagerStatsCommand(this);
    }

    // 6. Logica de Business
    public void navigheaza(String rutaFxml) {
        ultimaRuta = rutaFxml;
        if (actiuneNavigare != null) {
            actiuneNavigare.accept(rutaFxml);
        }
    }

    public void incarcaPaginaSalvata() {
        if (actiuneNavigare != null) {
            actiuneNavigare.accept(ultimaRuta);
        }
    }

    // 7. Setters & Triggers
    public void setActiuneNavigare(Consumer<String> actiuneNavigare) {
        this.actiuneNavigare = actiuneNavigare;
    }

    // 8. Getters
    public ICommand getNavZboruriCommand() { return navZboruriCommand; }
    public ICommand getNavStatisticiCommand() { return navStatisticiCommand; }
}