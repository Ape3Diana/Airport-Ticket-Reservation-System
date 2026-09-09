package com.aeroport.viewmodel;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.unauthenticated.SearchFlightsCommand;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class UnauthenticatedFlightViewModel {

    // 1. Servicii
    private final RestServiceClient restClient = new RestServiceClient();

    // 2. Proprietăți de Stare
    private final StringProperty plecare = new SimpleStringProperty("");
    private final StringProperty sosire = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dataZbor = new SimpleObjectProperty<>(LocalDate.now());
    private final ObservableList<List<String>> listaZboruriAfisare = FXCollections.observableArrayList();
    private final StringProperty mesajEroare = new SimpleStringProperty("");

    // 3. Comenzi
    private final ICommand searchFlightsCommand;

    // 4. Callbacks
    private Consumer<String> onShowErrorPopup;

    // 5. Constructor
    public UnauthenticatedFlightViewModel() {
        this.searchFlightsCommand = new SearchFlightsCommand(this, restClient);
    }

    // 6. Logica de Business
    public void actualizeazaListaZboruri(List<GenericDataModel> rezultateRaw) {
        listaZboruriAfisare.clear();
        for (GenericDataModel m : rezultateRaw) {
            listaZboruriAfisare.add(m.getInfo());
        }
    }

    // 7. Setters & Triggers
    public void afiseazaMesajStatus(String mesaj) {
        this.mesajEroare.set(mesaj);
    }

    public void setOnShowErrorPopup(Consumer<String> callback) {
        this.onShowErrorPopup = callback;
    }

    public void triggerErrorPopup(String msg) {
        if (onShowErrorPopup != null) {
            onShowErrorPopup.accept(msg);
        }
    }

    // 8. Getters
    public ICommand getSearchFlightsCommand() { return searchFlightsCommand; }
    public StringProperty plecareProperty() { return plecare; }
    public StringProperty sosireProperty() { return sosire; }
    public ObjectProperty<LocalDate> dataZborProperty() { return dataZbor; }
    public ObservableList<List<String>> getListaZboruriAfisare() { return listaZboruriAfisare; }
    public StringProperty mesajEroareProperty() { return mesajEroare; }
}