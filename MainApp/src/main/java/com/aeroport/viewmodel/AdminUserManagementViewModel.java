package com.aeroport.viewmodel;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.administrator.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class AdminUserManagementViewModel {

    // 1. Servicii / Utilitare
    private final RestServiceClient restClient = new RestServiceClient();

    // 2. Proprietăți JavaFX (Stare)
    private final ObservableList<List<String>> listaUtilizatoriAfisare = FXCollections.observableArrayList();
    private final ObservableList<GenericDataModel> listaBruta = FXCollections.observableArrayList();
    private final ObservableList<String> optiuniRoluriFormular = FXCollections.observableArrayList();
    private final ObservableList<String> optiuniRoluriFiltru = FXCollections.observableArrayList();

    private final StringProperty idSelectat = new SimpleStringProperty("");
    private final StringProperty numeComplet = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty telefon = new SimpleStringProperty("");
    private final StringProperty parola = new SimpleStringProperty("");
    private final StringProperty rolSelectat = new SimpleStringProperty("");
    private final StringProperty status = new SimpleStringProperty("");
    private final StringProperty rolFiltruSelectat = new SimpleStringProperty("");
    private final ObjectProperty<List<String>> selectedUser = new SimpleObjectProperty<>();

    // 3. Comenzi (ICommand)
    private final ICommand saveCommand;
    private final ICommand deleteCommand;
    private final ICommand exportCommand;
    private final ICommand filterCommand;

    // 4. Callbacks către View
    private Consumer<String> onShowSuccessPopup;
    private Consumer<String> onShowErrorPopup;

    // 5. Constructor (Curat, fără UI)
    public AdminUserManagementViewModel() {
        this.saveCommand = new SaveUserCommand(this, restClient);
        this.deleteCommand = new DeleteUserCommand(this, restClient);
        this.exportCommand = new ExportCsvCommand(this, restClient);
        this.filterCommand = new FilterUserCommand(this, restClient);

        this.selectedUser.addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                incarcaInFormular(newSel);
            }
        });

        incarcaOptiuniRoluri();
    }

    // 6. Logica de Business
    public void incarcaOptiuniRoluri() {
        ResourceBundle bundle = LanguageManager.getBundle();
        optiuniRoluriFormular.setAll(
                bundle.getString("role.admin"),
                bundle.getString("role.manager"),
                bundle.getString("role.employee")
        );
        optiuniRoluriFiltru.setAll(
                bundle.getString("admin.filter.all"),
                bundle.getString("role.admin"),
                bundle.getString("role.manager"),
                bundle.getString("role.employee")
        );
    }

    public String getTranslatedRole(String backendRole) {
        ResourceBundle bundle = LanguageManager.getBundle();
        if (backendRole == null) return "";
        return switch (backendRole.toUpperCase()) {
            case "ADMINISTRATOR" -> bundle.getString("role.admin");
            case "MANAGER" -> bundle.getString("role.manager");
            case "ANGAJAT" -> bundle.getString("role.employee");
            default -> backendRole;
        };
    }

    public String getBackendRole(String translatedRole) {
        ResourceBundle bundle = LanguageManager.getBundle();
        if (translatedRole == null) return null;
        if (translatedRole.equals(bundle.getString("role.admin"))) return "ADMINISTRATOR";
        if (translatedRole.equals(bundle.getString("role.manager"))) return "MANAGER";
        if (translatedRole.equals(bundle.getString("role.employee"))) return "ANGAJAT";
        return "Toți";
    }

    public void actualizeazaListaUtilizatori(List<GenericDataModel> users) {
        listaBruta.setAll(users);
        listaUtilizatoriAfisare.clear();
        for (GenericDataModel m : users) {
            List<String> rand = new java.util.ArrayList<>();
            List<String> info = m.getInfo();
            rand.add(m.getId());
            rand.add(info.size() > 0 ? info.get(0) : "");
            rand.add(info.size() > 2 ? info.get(2) : "");
            String rolBackend = info.size() > 3 ? info.get(3) : "";
            rand.add(getTranslatedRole(rolBackend));
            rand.add(info.size() > 4 ? info.get(4) : "");
            rand.add(info.size() > 1 ? info.get(1) : "");
            rand.add(info.size() > 5 ? info.get(5) : "");
            listaUtilizatoriAfisare.add(rand);
        }
    }

    public void incarcaInFormular(List<String> randSelectat) {
        idSelectat.set(randSelectat.get(0));
        email.set(randSelectat.get(1));
        numeComplet.set(randSelectat.get(2));
        rolSelectat.set(randSelectat.get(3));
        telefon.set(randSelectat.get(4));
        parola.set(randSelectat.get(5));
        status.set(LanguageManager.getBundle().getString("status.user_selected"));
    }

    public void curataFormular() {
        idSelectat.set("");
        numeComplet.set("");
        email.set("");
        telefon.set("");
        parola.set("");
        rolSelectat.set(null);
        status.set(LanguageManager.getBundle().getString("status.form_cleared"));
    }

    public void incarcaUtilizatori() {
        if (filterCommand != null) filterCommand.execute();
    }

    // 7. Setters & Triggers pentru Callbacks
    public void setOnShowSuccessPopup(Consumer<String> callback) { this.onShowSuccessPopup = callback; }
    public void setOnShowErrorPopup(Consumer<String> callback) { this.onShowErrorPopup = callback; }

    public void triggerSuccess(String msg) {
        status.set(msg);
        if (onShowSuccessPopup != null) onShowSuccessPopup.accept(msg);
    }

    public void triggerError(String msg) {
        status.set(msg);
        if (onShowErrorPopup != null) onShowErrorPopup.accept(msg);
    }

    // 8. Getters (Proprietăți și Comenzi)
    public String getIdSelectat() { return idSelectat.get(); }
    public ObservableList<List<String>> getListaUtilizatoriAfisare() { return listaUtilizatoriAfisare; }
    public ObservableList<String> getOptiuniRoluriFormular() { return optiuniRoluriFormular; }
    public ObservableList<String> getOptiuniRoluriFiltru() { return optiuniRoluriFiltru; }
    public StringProperty numeCompletProperty() { return numeComplet; }
    public StringProperty emailProperty() { return email; }
    public StringProperty telefonProperty() { return telefon; }
    public StringProperty parolaProperty() { return parola; }
    public StringProperty rolSelectatProperty() { return rolSelectat; }
    public StringProperty statusProperty() { return status; }
    public StringProperty rolFiltruSelectatProperty() { return rolFiltruSelectat; }
    public ObjectProperty<List<String>> selectedUserProperty() { return selectedUser; }

    public ICommand getSaveCommand() { return saveCommand; }
    public ICommand getDeleteCommand() { return deleteCommand; }
    public ICommand getExportCommand() { return exportCommand; }
    public ICommand getFilterCommand() { return filterCommand; }
}