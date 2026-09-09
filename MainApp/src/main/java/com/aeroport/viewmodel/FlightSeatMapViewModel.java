package com.aeroport.viewmodel;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.commands.ICommand;
import com.aeroport.viewmodel.commands.angajat.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FlightSeatMapViewModel {

    // 1. Servicii / Utilitare
    private final RestServiceClient restClient = new RestServiceClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();

    // 2. Proprietăți de Stare (JavaFX Properties / Variabile interne)
    private final StringProperty mesajStatus = new SimpleStringProperty("");
    private final StringProperty numePasager = new SimpleStringProperty("");
    private final StringProperty numarLoc = new SimpleStringProperty("");
    private final StringProperty dataVanzare = new SimpleStringProperty("");

    private final Map<String, StringProperty> seatStyles = new HashMap<>();
    private final Map<String, BooleanProperty> seatDisables = new HashMap<>();

    private final BooleanProperty vanzareDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty actualizareDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty anulareDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty exportPdfDisabled = new SimpleBooleanProperty(true);

    private final List<Map<String, Object>> bileteVandute = new ArrayList<>();

    private int idBiletCurent = -1;
    private int idZborCurent;
    private double pretCurent;
    private int capacitateZborCalculata;
    private String numarZborInfo = "";
    private String plecareInfo = "";
    private String sosireInfo = "";
    private String oraDecolareInfo = "";
    private String oraAterizareInfo = "";

    // 3. Comenzi (ICommand)
    private final ICommand vindeBiletCommand;
    private final ICommand actualizeazaBiletCommand;
    private final ICommand anuleazaBiletCommand;
    private final ICommand exportPdfCommand;

    // 4. Callbacks
    private Consumer<Void> onRefreshMatrix;
    private Consumer<String> onShowErrorPopup;

    // 5. Constructor
    public FlightSeatMapViewModel() {
        this.vindeBiletCommand = new SaveBiletCommand(this, restClient);
        this.actualizeazaBiletCommand = new UpdateBiletCommand(this, restClient);
        this.anuleazaBiletCommand = new DeleteBiletCommand(this, restClient);
        this.exportPdfCommand = new ExportBiletPdfCommand(this, restClient);
    }

    // 6. Logica de Business
    public void initializeazaZbor(int idZbor, double pret) {
        this.idZborCurent = idZbor;
        this.pretCurent = pret;
        incarcaBilete();
    }

    public void incarcaBilete() {
        try {
            String url = restClient.getUrlBilete() + "/zbor/" + idZborCurent;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            bileteVandute.clear();
            if (response.statusCode() == 200) {
                List<Map<String, Object>> date = mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
                bileteVandute.addAll(date);
            }
        } catch (Exception e) {
            triggerError("Eroare la încărcare bilete: " + e.getMessage());
        }
    }

    public void calculeazaCapacitateReala(int locuriLibereInitiale) {
        this.capacitateZborCalculata = locuriLibereInitiale + bileteVandute.size();
        precalculareStariScaune();
    }

    private void precalculareStariScaune() {
        int locCurent = 0;
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 6; col++) {
                char litera = (char) ('A' + col);
                String denumireLoc = (row + 1) + String.valueOf(litera);

                StringProperty styleProp = seatStyleProperty(denumireLoc);
                BooleanProperty disableProp = seatDisableProperty(denumireLoc);

                Map<String, Object> bilet = getBiletPentruLoc(denumireLoc);

                if (locCurent >= capacitateZborCalculata) {
                    styleProp.set("-fx-background-color: #95a5a6; -fx-text-fill: white;");
                    disableProp.set(true);
                } else if (bilet != null) {
                    styleProp.set("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                    disableProp.set(false);
                } else {
                    styleProp.set("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    disableProp.set(false);
                }
                locCurent++;
            }
        }
        incarcaLocInFormular("", null);
    }

    public void selecteazaLoc(String denumireLoc) {
        Map<String, Object> biletExistente = getBiletPentruLoc(denumireLoc);
        incarcaLocInFormular(denumireLoc, biletExistente);

        if (biletExistente != null) {
            vanzareDisabled.set(true);
            actualizareDisabled.set(false);
            anulareDisabled.set(false);
            exportPdfDisabled.set(false);
        } else {
            vanzareDisabled.set(false);
            actualizareDisabled.set(true);
            anulareDisabled.set(true);
            exportPdfDisabled.set(true);
        }
    }

    public void incarcaLocInFormular(String loc, Map<String, Object> biletExistente) {
        numarLoc.set(loc);
        mesajStatus.set("");

        if (biletExistente != null) {
            Object idRaw = biletExistente.get("id");
            this.idBiletCurent = (idRaw instanceof Map) ? (Integer)((Map)idRaw).get("id") : (Integer)idRaw;

            Object numeObj = biletExistente.get("numePasager");
            numePasager.set(numeObj != null ? numeObj.toString() : "");

            Object dataObj = biletExistente.get("dataAchizitie");
            dataVanzare.set(dataObj != null ? dataObj.toString().replace("T", " ") : "");
        } else {
            this.idBiletCurent = -1;
            numePasager.set("");
            dataVanzare.set("");
        }
    }

    public void triggerRefresh(String mesaj) {
        incarcaBilete();
        precalculareStariScaune();
        mesajStatus.set(mesaj);
        if (onRefreshMatrix != null) onRefreshMatrix.accept(null);
    }

    public Map<String, Object> getBiletPentruLoc(String numarLoc) {
        return bileteVandute.stream()
                .filter(b -> b.get("numarLoc") != null && b.get("numarLoc").toString().equalsIgnoreCase(numarLoc))
                .findFirst().orElse(null);
    }

    public void setDetaliiZbor(String nr, String plecare, String sosire, String oraDec, String oraAter) {
        this.numarZborInfo = nr;
        this.plecareInfo = plecare;
        this.sosireInfo = sosire;
        this.oraDecolareInfo = oraDec;
        this.oraAterizareInfo = oraAter;
    }

    // 7. Setters & Triggers pentru Callbacks
    public void setOnShowErrorPopup(Consumer<String> callback) { this.onShowErrorPopup = callback; }
    public void setOnRefreshMatrix(Consumer<Void> callback) { this.onRefreshMatrix = callback; }

    public void triggerError(String mesaj) {
        if (onShowErrorPopup != null) onShowErrorPopup.accept(mesaj);
    }

    // 8. Getters
    public List<Map<String, Object>> getBileteVandute() { return bileteVandute; }
    public int getIdZborCurent() { return idZborCurent; }
    public double getPretCurent() { return pretCurent; }
    public int getIdBiletCurent() { return idBiletCurent; }
    public int getCapacitateZborCalculata() { return capacitateZborCalculata; }
    public String getNumarZborInfo(){return numarZborInfo;}
    public String getPlecareInfo() { return plecareInfo; }
    public String getSosireInfo() { return sosireInfo; }
    public String getOraDecolareInfo() { return oraDecolareInfo; }
    public String getOraAterizareInfo() { return oraAterizareInfo; }

    public StringProperty mesajStatusProperty() { return mesajStatus; }
    public StringProperty numePasagerProperty() { return numePasager; }
    public StringProperty numarLocProperty() { return numarLoc; }
    public StringProperty dataVanzareProperty() { return dataVanzare; }

    public BooleanProperty vanzareDisabledProperty() { return vanzareDisabled; }
    public BooleanProperty actualizareDisabledProperty() { return actualizareDisabled; }
    public BooleanProperty anulareDisabledProperty() { return anulareDisabled; }
    public BooleanProperty exportPdfDisabledProperty() { return exportPdfDisabled; }

    public ICommand getVindeBiletCommand() { return vindeBiletCommand; }
    public ICommand getActualizeazaBiletCommand() { return actualizeazaBiletCommand; }
    public ICommand getAnuleazaBiletCommand() { return anuleazaBiletCommand; }
    public ICommand getExportPdfCommand() { return exportPdfCommand; }

    public StringProperty seatStyleProperty(String seatName) {
        return seatStyles.computeIfAbsent(seatName, k -> new SimpleStringProperty("-fx-background-color: #2ecc71; -fx-text-fill: white;"));
    }

    public BooleanProperty seatDisableProperty(String seatName) {
        return seatDisables.computeIfAbsent(seatName, k -> new SimpleBooleanProperty(false));
    }
}