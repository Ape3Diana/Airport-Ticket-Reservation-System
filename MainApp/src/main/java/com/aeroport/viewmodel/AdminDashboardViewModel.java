package com.aeroport.viewmodel;

import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.administrator.NavUtilizatoriCommand;
import com.aeroport.viewmodel.commands.administrator.NavZboruriCommand;
import java.util.function.Consumer;

public class AdminDashboardViewModel {

    // 1. Variabile de stare statice/interne
    private static String ultimaRuta = "/fxml/AdminUserManagementView.fxml";

    // 2. Comenzi
    private final ICommand navZboruriCommand;
    private final ICommand navUtilizatoriCommand;

    // 3. Callbacks
    private Consumer<String> actiuneNavigare;

    // 4. Constructor
    public AdminDashboardViewModel() {
        this.navZboruriCommand = new NavZboruriCommand(this);
        this.navUtilizatoriCommand = new NavUtilizatoriCommand(this);
    }

    // 5. Logica de Business
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

    // 6. Setters Callbacks
    public void setActiuneNavigare(Consumer<String> actiuneNavigare) {
        this.actiuneNavigare = actiuneNavigare;
    }

    // 7. Getters Comenzi
    public ICommand getNavZboruriCommand() { return navZboruriCommand; }
    public ICommand getNavUtilizatoriCommand() { return navUtilizatoriCommand; }
}