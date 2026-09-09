package com.aeroport.viewmodel.commands.manager;

import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.ManagerFlightManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Comandă complexă responsabilă de crearea și actualizarea detaliilor unui zbor (Operațiuni POST / PUT).
 * Mapează obiectele complexe (Aeroport de Plecare/Sosire) pe bază de ID și compune string-urile ISO pentru date.
 */
public class SaveFlightCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final ManagerFlightManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public SaveFlightCommand(ManagerFlightManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică (Salvare / Formular Mapping)
    // =========================================================================

    @Override
    public void execute() {
        ResourceBundle bundle = LanguageManager.getBundle();

        // Validare defensivă elementară: Numarul de zbor este un parametru obligatoriu în sistem
        if (viewModel.numarZborProperty().get() == null || viewModel.numarZborProperty().get().trim().isEmpty()) {
            viewModel.triggerError(bundle.getString("error.fields"));
            return;
        }

        // Identificare mod de lucru (Câmpul ID prezent implică operațiunea de Update)
        String idSelectat = viewModel.getIdSelectat();
        boolean esteUpdate = (idSelectat != null && !idSelectat.trim().isEmpty());

        // Maparea payload-ului JSON general pentru transmiterea spre server
        Map<String, Object> dateZbor = new HashMap<>();

        if (esteUpdate) {
            dateZbor.put("id", Integer.parseInt(idSelectat));
        }

        dateZbor.put("numarZbor", viewModel.numarZborProperty().get());

        // Extragerea ID-urilor unice ale aeroporturilor pe baza denumirilor selectate în ComboBox
        String idPlecare = viewModel.getIdAeroportDupaNumeAfisat(viewModel.plecareProperty().get());
        String idSosire = viewModel.getIdAeroportDupaNumeAfisat(viewModel.sosireProperty().get());

        // Încapsularea sub-obiectelor relaționale conform schemei bazei de date din backend
        Map<String, Object> aeroportPlecare = new HashMap<>();
        aeroportPlecare.put("id", Integer.parseInt(idPlecare));

        Map<String, Object> aeroportSosire = new HashMap<>();
        aeroportSosire.put("id", Integer.parseInt(idSosire));

        dateZbor.put("aeroportPlecare", aeroportPlecare);
        dateZbor.put("aeroportSosire", aeroportSosire);

        // Concatenarea Datei din DatePicker cu orele textuale pentru formarea formatului extins ISO_LOCAL_DATE_TIME (T)
        dateZbor.put("oraDecolare", viewModel.dataZborProperty().get().toString() + "T" + viewModel.oraPlecareProperty().get());
        dateZbor.put("oraAterizare", viewModel.dataZborProperty().get().toString() + "T" + viewModel.oraSosireProperty().get());
        dateZbor.put("pretBilet", Double.parseDouble(viewModel.pretProperty().get()));
        dateZbor.put("locuriDisponibile", Integer.parseInt(viewModel.locuriProperty().get()));

        try {
            // Executarea apelului de persistență specific modului determinat
            if (esteUpdate) {
                restClient.putData(restClient.getUrlZboruri() + "/" + idSelectat, dateZbor);
            } else {
                restClient.postData(restClient.getUrlZboruri(), dateZbor);
            }

            // Notificarea succesului, golirea câmpurilor din formular și împrospătarea listei
            viewModel.statusProperty().set(bundle.getString("status.save_success"));
            viewModel.curataFormular();
            viewModel.getLoadAllFlightsManagerCommand().execute();

        } catch (Exception e) {
            // Propagarea erorilor de rețea sau de validare date (formate incorecte de oră sau numere) spre pop-up
            viewModel.triggerError(bundle.getString("error.connection") + " " + e.getMessage());
        }
    }
}