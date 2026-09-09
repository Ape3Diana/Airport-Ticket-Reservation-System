package com.aeroport.viewmodel;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.manager.LoadStatsCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ManagerStatsViewModel {

    // 1. Servicii / Utilitare
    private final RestServiceClient restClient = new RestServiceClient();

    // 2. Proprietăți de Stare (Listele pentru grafice)
    private final List<Map<String, Object>> dataVenituri = new ArrayList<>();
    private final List<Map<String, Object>> dataOcupare = new ArrayList<>();
    private final List<Map<String, Object>> dataDestinatii = new ArrayList<>();

    // 3. Comenzi (ICommand)
    private final ICommand loadStatsCommand;

    // 4. Callbacks
    private Runnable onDataLoadedCallback;
    private Consumer<String> onShowErrorPopup;

    // 5. Constructor
    public ManagerStatsViewModel() {
        this.loadStatsCommand = new LoadStatsCommand(this, restClient);
    }

    // 6. Logica de Business
    // (Aici logica principală este delegată comenzii LoadStatsCommand)

    // 7. Setters & Triggers
    public void setOnDataLoadedCallback(Runnable callback) { this.onDataLoadedCallback = callback; }
    public void setOnShowErrorPopup(Consumer<String> callback) { this.onShowErrorPopup = callback; }

    public void notificaDateIncarcate() {
        if (onDataLoadedCallback != null) {
            onDataLoadedCallback.run();
        }
    }

    public void triggerError(String mesaj) {
        if (onShowErrorPopup != null) {
            onShowErrorPopup.accept(mesaj);
        }
    }

    // 8. Getters pentru date brute și comenzi
    public List<Map<String, Object>> getDataVenituri() { return dataVenituri; }
    public List<Map<String, Object>> getDataOcupare() { return dataOcupare; }
    public List<Map<String, Object>> getDataDestinatii() { return dataDestinatii; }

    public ICommand getLoadStatsCommand() { return loadStatsCommand; }
}