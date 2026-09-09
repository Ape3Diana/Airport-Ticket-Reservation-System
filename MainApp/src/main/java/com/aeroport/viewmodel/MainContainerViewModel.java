package com.aeroport.viewmodel;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.main.ChangeLanguageCommand;
import com.aeroport.viewmodel.commands.main.LoginCommand;
import com.aeroport.viewmodel.commands.main.LogoutCommand;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainContainerViewModel {

    // 1. Servicii
    private final RestServiceClient restClient = new RestServiceClient();

    // 2. Proprietăți de stare
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty parola = new SimpleStringProperty("");
    private final StringProperty mesajStatus = new SimpleStringProperty("");
    private final BooleanProperty autentificat = new SimpleBooleanProperty(false);
    private final StringProperty infoUtilizator = new SimpleStringProperty("");
    private final StringProperty limbaSelectata = new SimpleStringProperty("Română");

    private final StringProperty labelEmailText = new SimpleStringProperty("");
    private final StringProperty labelParolaText = new SimpleStringProperty("");
    private final StringProperty buttonLoginText = new SimpleStringProperty("");
    private final StringProperty labelLimbaText = new SimpleStringProperty("");
    private final StringProperty buttonLogoutText = new SimpleStringProperty("");

    private String numeUtilizatorLogat = "";
    private String rolBrutUtilizator = "";
    private String rolCurentEcran = "";

    // 3. Comenzi
    private final ICommand loginCommand;
    private final ICommand logoutCommand;
    private final ICommand changeLanguageCommand;

    // 4. Callbacks
    private Consumer<String> actiuneSchimbaEcran;
    private Consumer<String> onShowError;
    private Consumer<List<Map<String, Object>>> onShowNotifications;

    // 5. Constructor
    public MainContainerViewModel() {
        this.loginCommand = new LoginCommand(this, restClient);
        this.logoutCommand = new LogoutCommand(this);
        this.changeLanguageCommand = new ChangeLanguageCommand(this);

        actualizeazaTexteDupaLimba();
    }

    // 6. Logica de Business
    public void incarcaEcranInitial() {
        if (actiuneSchimbaEcran != null) actiuneSchimbaEcran.accept("/fxml/UnauthenticatedFlightView.fxml");
    }

    public void proceseazaLoginSucces(String nume, String rol) {
        this.numeUtilizatorLogat = nume;
        this.rolBrutUtilizator = rol;
        this.rolCurentEcran = rol;
        this.autentificat.set(true);
        this.mesajStatus.set("");
        actualizeazaTexteDupaLimba();
        determinaEcranDupaRol(rol);
    }

    public void proceseazaLoginEsec(String mesajEroare) {
        this.mesajStatus.set(mesajEroare);
        triggerErrorPopup(mesajEroare);
    }

    public void reseteazaDateSesiune() {
        this.autentificat.set(false);
        this.infoUtilizator.set("");
        this.email.set("");
        this.parola.set("");
        this.numeUtilizatorLogat = "";
        this.rolBrutUtilizator = "";
        this.rolCurentEcran = "";
    }

    public void actualizeazaTexteDupaLimba() {
        ResourceBundle bundle = LanguageManager.getBundle();

        labelEmailText.set(bundle.getString("login.email"));
        labelParolaText.set(bundle.getString("login.password"));
        buttonLoginText.set(bundle.getString("login.button"));
        labelLimbaText.set(bundle.getString("lang.label"));
        buttonLogoutText.set(bundle.getString("header.logout"));

        if (autentificat.get() && !rolBrutUtilizator.isEmpty()) {
            String rolTradus = switch (rolBrutUtilizator.toUpperCase()) {
                case "ADMINISTRATOR" -> bundle.getString("role.admin");
                case "MANAGER" -> bundle.getString("role.manager");
                case "ANGAJAT" -> bundle.getString("role.employee");
                default -> rolBrutUtilizator;
            };
            this.infoUtilizator.set(rolTradus + " - " + this.numeUtilizatorLogat);
        }
    }

    private void determinaEcranDupaRol(String rol) {
        this.rolCurentEcran = rol;
        String ruta = switch (rol.toUpperCase()) {
            case "ANGAJAT" -> "/fxml/EmployeeDashboardView.fxml";
            case "MANAGER" -> "/fxml/ManagerDashboardView.fxml";
            case "ADMINISTRATOR" -> "/fxml/AdminDashboardView.fxml";
            default -> "/fxml/UnauthenticatedFlightView.fxml";
        };

        if (actiuneSchimbaEcran != null) actiuneSchimbaEcran.accept(ruta);
    }

    public void reincarcaEcranCurent() {
        determinaEcranDupaRol(this.rolCurentEcran);
    }

    // 7. Setters & Triggers
    public void setActiuneSchimbaEcran(Consumer<String> actiuneSchimbaEcran) { this.actiuneSchimbaEcran = actiuneSchimbaEcran; }
    public void setOnShowError(Consumer<String> callback) { this.onShowError = callback; }
    public void setOnShowNotifications(Consumer<List<Map<String, Object>>> callback) { this.onShowNotifications = callback; }

    public void triggerErrorPopup(String msg) {
        if (onShowError != null) onShowError.accept(msg);
    }

    public void triggerNotifications(List<Map<String, Object>> notificari) {
        if (onShowNotifications != null && notificari != null && !notificari.isEmpty()) {
            onShowNotifications.accept(notificari);
        }
    }

    // 8. Getters
    public ICommand getLoginCommand() { return loginCommand; }
    public ICommand getLogoutCommand() { return logoutCommand; }
    public ICommand getChangeLanguageCommand() { return changeLanguageCommand; }

    public StringProperty labelEmailTextProperty() { return labelEmailText; }
    public StringProperty labelParolaTextProperty() { return labelParolaText; }
    public StringProperty buttonLoginTextProperty() { return buttonLoginText; }
    public StringProperty labelLimbaTextProperty() { return labelLimbaText; }
    public StringProperty buttonLogoutTextProperty() { return buttonLogoutText; }

    public StringProperty emailProperty() { return email; }
    public StringProperty parolaProperty() { return parola; }
    public StringProperty mesajStatusProperty() { return mesajStatus; }
    public BooleanProperty autentificatProperty() { return autentificat; }
    public StringProperty infoUtilizatorProperty() { return infoUtilizator; }
    public StringProperty limbaSelectataProperty() { return limbaSelectata; }
}