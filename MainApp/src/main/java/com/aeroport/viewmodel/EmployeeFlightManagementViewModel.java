package com.aeroport.viewmodel;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.angajat.SearchFlightsEmployeeCommand;
import com.aeroport.viewmodel.commands.angajat.LoadAllFlightsEmployeeCommand;
import com.aeroport.viewmodel.commands.angajat.ExportTicketsEmployeeCommand;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.function.Consumer;

public class EmployeeFlightManagementViewModel {

    private final RestServiceClient restClient = new RestServiceClient();

    private final ObservableList<List<String>> listaZboruri = FXCollections.observableArrayList();
    private final ObservableList<String> formateExport = FXCollections.observableArrayList("csv", "json", "xml", "doc");

    private final StringProperty textCautare = new SimpleStringProperty("");
    private final StringProperty formatExport = new SimpleStringProperty("csv");
    private final IntegerProperty idZborSelectat = new SimpleIntegerProperty(-1);
    private final BooleanProperty afiseazaEroare = new SimpleBooleanProperty(false);
    private final ObjectProperty<List<String>> zborSelectat = new SimpleObjectProperty<>(null);
    private final BooleanProperty exportDisabled = new SimpleBooleanProperty(true);
    private final StringProperty numarZborSelectat = new SimpleStringProperty("");
    private final StringProperty mesajEroare = new SimpleStringProperty("");
    private final StringProperty mesajSucces = new SimpleStringProperty("");

    private Consumer<String> errorPopupAction;

    private final ICommand searchFlightsEmployeeCommand;
    private final ICommand loadAllFlightsEmployeeCommand;
    private final ICommand exportTicketsEmployeeCommand;

    public EmployeeFlightManagementViewModel() {
        this.searchFlightsEmployeeCommand = new SearchFlightsEmployeeCommand(this, restClient);
        this.loadAllFlightsEmployeeCommand = new LoadAllFlightsEmployeeCommand(this, restClient);
        this.exportTicketsEmployeeCommand = new ExportTicketsEmployeeCommand(this, restClient);

        this.zborSelectat.addListener((obs, vechi, nou) -> {
            this.exportDisabled.set(nou == null);
            this.idZborSelectat.set(nou != null ? Integer.parseInt(nou.get(0)) : -1);
            this.numarZborSelectat.set(nou != null ? nou.get(1) : "");
        });

        this.mesajEroare.addListener((obs, vechi, nou) -> {
            if (nou != null && !nou.isEmpty() && errorPopupAction != null) {
                errorPopupAction.accept(nou);
            }
        });
    }

    public void actualizeazaListaZboruri(List<GenericDataModel> rezultateRaw) {
        listaZboruri.clear();
        for (GenericDataModel m : rezultateRaw) {
            List<String> randComplet = new java.util.ArrayList<>();
            randComplet.add(m.getId());
            randComplet.addAll(m.getInfo());
            listaZboruri.add(randComplet);
        }
    }

    // Singura metodă rămasă pentru că View-ul are nevoie să șteargă starea erorii după închiderea pop-up-ului
    public void clearError() { mesajEroare.set(""); }

    public void setErrorPopupAction(Consumer<String> errorPopupAction) { this.errorPopupAction = errorPopupAction; }

    public ObservableList<String> getFormateExport() { return formateExport; }

    // GETTERII COMENZILOR - Acum vor fi folosiți direct de View!
    public ICommand getSearchFlightsEmployeeCommand() { return searchFlightsEmployeeCommand; }
    public ICommand getLoadAllFlightsEmployeeCommand() { return loadAllFlightsEmployeeCommand; }
    public ICommand getExportTicketsEmployeeCommand() { return exportTicketsEmployeeCommand; }

    public StringProperty textCautareProperty() { return textCautare; }
    public ObservableList<List<String>> getListaZboruri() { return listaZboruri; }
    public StringProperty formatExportProperty() { return formatExport; }
    public IntegerProperty idZborSelectatProperty() { return idZborSelectat; }
    public BooleanProperty afiseazaEroareProperty() { return afiseazaEroare; }
    public ObjectProperty<List<String>> zborSelectatProperty() { return zborSelectat; }
    public BooleanProperty exportDisabledProperty() { return exportDisabled; }
    public StringProperty numarZborSelectatProperty() { return numarZborSelectat; }
    public StringProperty mesajSuccesProperty() { return mesajSucces; }
    public StringProperty mesajEroareProperty() { return mesajEroare; }
}