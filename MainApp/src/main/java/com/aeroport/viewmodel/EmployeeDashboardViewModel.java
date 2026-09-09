package com.aeroport.viewmodel;

import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.angajat.NavCautaZboruriCommand;
import com.aeroport.viewmodel.commands.angajat.NavGestiuneZboruriCommand;
import java.util.function.Consumer;

public class EmployeeDashboardViewModel {

    // 1. Servicii / Utilitare
    // (Nu sunt necesare servicii de rețea în acest Dashboard)

    // 2. Proprietăți de Stare / Variabile statice
    private static String ultimaRuta = "/fxml/EmployeeFlightManagementView.fxml";

    // 3. Comenzi (ICommand)
    private final ICommand navCautaZboruriCommand;
    private final ICommand navGestiuneZboruriCommand;

    // 4. Callbacks
    private Consumer<String> actiuneNavigare;

    // 5. Constructor
    public EmployeeDashboardViewModel() {
        this.navCautaZboruriCommand = new NavCautaZboruriCommand(this);
        this.navGestiuneZboruriCommand = new NavGestiuneZboruriCommand(this);
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
    public ICommand getNavCautaZboruriCommand() { return navCautaZboruriCommand; }
    public ICommand getNavGestiuneZboruriCommand() { return navGestiuneZboruriCommand; }
}