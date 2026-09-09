package com.aeroport.viewmodel.commands.administrator;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.AdminUserManagementViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Comandă dedicată filtrării listei de utilizatori din panoul de administrare.
 * Citește valoarea selectată în UI, o convertește în tipul recunoscut de backend și actualizează colecția observabilă.
 */
public class FilterUserCommand implements ICommand {

    // =========================================================================
    // 1. Atribute și Dependențe
    // =========================================================================

    private final AdminUserManagementViewModel viewModel;
    private final RestServiceClient restClient;

    // =========================================================================
    // 2. Constructor
    // =========================================================================

    public FilterUserCommand(AdminUserManagementViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    // =========================================================================
    // 3. Execuție Logică
    // =========================================================================

    /**
     * Interoghează starea filtrului curent. Dacă filtrul cere toți utilizatorii, efectuează o descărcare globală,
     * altfel aplică parametrul de interogare (query parameter) corerespunzător rolului selectat.
     */
    @Override
    public void execute() {
        try {
            ResourceBundle bundle = LanguageManager.getBundle();
            String txtToti = bundle.getString("admin.filter.all");

            // Citirea automată a valorii textuale din selectorul interfeței grafice
            String translatedRole = viewModel.rolFiltruSelectatProperty().get();

            // Evaluare logică: caz general (Toți) sau caz specific (Filtrare per rol)
            if (translatedRole == null || translatedRole.isEmpty() || translatedRole.equals(txtToti)) {
                // Descărcarea listei complete brute
                List<GenericDataModel> users = restClient.getListData(restClient.getUrlUtilizatori());
                viewModel.actualizeazaListaUtilizatori(users);
            } else {
                // Transpunerea denumirii traduse (ex: "Angajat") în codul intern de backend (ex: "EMPLOYEE")
                String backendTip = viewModel.getBackendRole(translatedRole);
                String urlFiltru = restClient.getUrlUtilizatori() + "/filtru?tip=" + backendTip;

                // Încărcarea datelor filtrate din microserviciu
                List<GenericDataModel> dateFiltrate = restClient.getListData(urlFiltru);
                viewModel.actualizeazaListaUtilizatori(dateFiltrate);
            }
        } catch (Exception e) {
            // Raportarea defecțiunilor de parsare REST direct pe ecran
            viewModel.triggerError(e.getMessage());
        }
    }
}