package com.aeroport.viewmodel;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.manager.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ManagerFlightManagementViewModel {

    // 1. Servicii
    private final RestServiceClient restClient = new RestServiceClient();

    // 2. Proprietăți JavaFX
    private final ObservableList<List<String>> listaZboruriAfisare = FXCollections.observableArrayList();
    private final ObservableList<GenericDataModel> aeroporturiBrute = FXCollections.observableArrayList();
    private final ObservableList<String> numeAeroporturi = FXCollections.observableArrayList();

    private final StringProperty plecareCauta = new SimpleStringProperty("");
    private final StringProperty sosireCauta = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataCauta = new SimpleObjectProperty<>(LocalDate.now());
    private final StringProperty cautaDupaNumar = new SimpleStringProperty("");
    private final ObjectProperty<List<String>> selectedFlight = new SimpleObjectProperty<>();
    private final StringProperty formatExport = new SimpleStringProperty("csv");

    private final StringProperty idSelectat = new SimpleStringProperty("");
    private final StringProperty numarZbor = new SimpleStringProperty("");
    private final StringProperty plecareSelectata = new SimpleStringProperty("");
    private final StringProperty sosireSelectata = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataZbor = new SimpleObjectProperty<>(LocalDate.now());
    private final StringProperty oraPlecare = new SimpleStringProperty("");
    private final StringProperty oraSosire = new SimpleStringProperty("");
    private final StringProperty pret = new SimpleStringProperty("");
    private final StringProperty locuri = new SimpleStringProperty("");
    private final StringProperty status = new SimpleStringProperty("");

    // 3. Comenzi
    private final ICommand saveCommand;
    private final ICommand deleteCommand;
    private final ICommand searchFlightsManagerCommand;
    private final ICommand filterByFlightNumberManagerCommand;
    private final ICommand loadAllFlightsManagerCommand;
    private final ICommand exportFlightsCommand;

    // 4. Callbacks
    private Consumer<String> onShowErrorPopup;

    // 5. Constructor
    public ManagerFlightManagementViewModel() {
        this.saveCommand = new SaveFlightCommand(this, restClient);
        this.deleteCommand = new DeleteFlightCommand(this, restClient);
        this.searchFlightsManagerCommand = new SearchFlightsManagerCommand(this, restClient);
        this.filterByFlightNumberManagerCommand = new FilterByFlightNumberManagerCommand(this, restClient);
        this.loadAllFlightsManagerCommand = new LoadAllFlightsManagerCommand(this, restClient);
        this.exportFlightsCommand = new ExportFlightsManagerCommand(this, restClient);

        incarcaAeroporturi();

        this.selectedFlight.addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                incarcaInFormular(newSel);
            }
        });
    }

    // 6. Logica de Business
    private void incarcaAeroporturi() {
        try {
            List<GenericDataModel> date = restClient.getListData(restClient.getUrlZboruri() + "/aeroporturi");
            aeroporturiBrute.setAll(date);
            numeAeroporturi.setAll(date.stream()
                    .map(a -> a.getInfo().get(0) + " (" + a.getInfo().get(3) + ")")
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            triggerError("Eroare la încărcarea aeroporturilor: " + e.getMessage());
        }
    }

    public void curataToateFiltrele() {
        plecareCauta.set("");
        sosireCauta.set("");
        dataCauta.set(LocalDate.now());
        cautaDupaNumar.set("");
    }

    public String getIdAeroportDupaNumeAfisat(String numeAfisat) {
        if (numeAfisat == null) return "";
        return aeroporturiBrute.stream()
                .filter(a -> (a.getInfo().get(0) + " (" + a.getInfo().get(3) + ")").equals(numeAfisat))
                .map(GenericDataModel::getId)
                .findFirst()
                .orElse("");
    }

    public void actualizeazaTabel(List<GenericDataModel> date) {
        listaZboruriAfisare.clear();
        for (GenericDataModel m : date) {
            List<String> rand = new java.util.ArrayList<>();
            rand.add(m.getId());
            rand.addAll(m.getInfo());
            listaZboruriAfisare.add(rand);
        }
    }

    public void incarcaInFormular(List<String> rand) {
        idSelectat.set(rand.get(0));
        numarZbor.set(rand.get(1));
        plecareSelectata.set(rand.get(2));
        sosireSelectata.set(rand.get(3));

        String plecareFull = rand.get(4);
        try {
            String[] parts = plecareFull.split("[T ]");
            dataZbor.set(LocalDate.parse(parts[0]));
            oraPlecare.set(parts.length > 1 ? parts[1] : "");
        } catch (Exception e) {
            dataZbor.set(LocalDate.now());
            oraPlecare.set(plecareFull);
        }

        String sosireFull = rand.get(5);
        try {
            String[] parts = sosireFull.split("[T ]");
            oraSosire.set(parts.length > 1 ? parts[1] : sosireFull);
        } catch (Exception e) {
            oraSosire.set(sosireFull);
        }

        pret.set(rand.get(6));
        locuri.set(rand.get(7));
        status.set(LanguageManager.getBundle().getString("status.flight_selected"));
    }

    public void curataFormular() {
        idSelectat.set("");
        numarZbor.set("");
        plecareSelectata.set(null);
        sosireSelectata.set(null);
        dataZbor.set(LocalDate.now());
        oraPlecare.set("");
        oraSosire.set("");
        pret.set("");
        locuri.set("");
        status.set(LanguageManager.getBundle().getString("status.form_cleared"));
    }

    // 7. Setters & Triggers
    public void setOnShowErrorPopup(Consumer<String> callback) { this.onShowErrorPopup = callback; }

    public void triggerError(String mesaj) {
        if (onShowErrorPopup != null) onShowErrorPopup.accept(mesaj);
    }

    // 8. Getters
    public ObservableList<String> getListaNumeAeroporturi() { return numeAeroporturi; }
    public StringProperty numarZborProperty() { return numarZbor; }
    public StringProperty plecareProperty() { return plecareSelectata; }
    public StringProperty sosireProperty() { return sosireSelectata; }
    public ObjectProperty<LocalDate> dataZborProperty() { return dataZbor; }
    public StringProperty oraPlecareProperty() { return oraPlecare; }
    public StringProperty oraSosireProperty() { return oraSosire; }
    public StringProperty pretProperty() { return pret; }
    public StringProperty locuriProperty() { return locuri; }
    public StringProperty statusProperty() { return status; }
    public StringProperty plecareCautaProperty() { return plecareCauta; }
    public StringProperty sosireCautaProperty() { return sosireCauta; }
    public ObjectProperty<LocalDate> dataCautaProperty() { return dataCauta; }
    public StringProperty cautaDupaNumarProperty() { return cautaDupaNumar; }
    public ObservableList<List<String>> getListaZboruriAfisare() { return listaZboruriAfisare; }
    public String getIdSelectat() { return idSelectat.get(); }
    public ObjectProperty<List<String>> selectedFlightProperty() { return selectedFlight; }
    public StringProperty formatExportProperty() { return formatExport; }

    public ICommand getExportFlightsCommand() { return exportFlightsCommand; }
    public ICommand getSearchFlightsManagerCommand() { return searchFlightsManagerCommand; }
    public ICommand getFilterByFlightNumberManagerCommand() { return filterByFlightNumberManagerCommand; }
    public ICommand getLoadAllFlightsManagerCommand() { return loadAllFlightsManagerCommand; }
    public ICommand getSaveCommand() { return saveCommand; }
    public ICommand getDeleteCommand() { return deleteCommand; }
}