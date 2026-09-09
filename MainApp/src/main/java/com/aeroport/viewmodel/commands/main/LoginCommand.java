package com.aeroport.viewmodel.commands.main;

import com.aeroport.model.GenericDataModel;
import com.aeroport.model.RestServiceClient;
import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.MainContainerViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.List;
import java.util.Map;

public class LoginCommand implements ICommand {
    private final MainContainerViewModel viewModel;
    private final RestServiceClient restClient;

    public LoginCommand(MainContainerViewModel viewModel, RestServiceClient restClient) {
        this.viewModel = viewModel;
        this.restClient = restClient;
    }

    @Override
    public void execute() {
        try {
            GenericDataModel user = restClient.login(viewModel.emailProperty().get(), viewModel.parolaProperty().get());

            if (user != null) {
                // Succes -> Pasăm datele
                viewModel.proceseazaLoginSucces(user.getInfo().get(2), user.getInfo().get(3));

                List<Map<String, Object>> notificari = restClient.getNotificariInAsteptare(user.getId());
                viewModel.triggerNotifications(notificari);
            } else {
                // Eșec -> Pasăm mesajul de eroare
                String msg = LanguageManager.getBundle().getString("error.login");
                viewModel.proceseazaLoginEsec(msg);
            }
        } catch (Exception e) {
            viewModel.proceseazaLoginEsec(LanguageManager.getBundle().getString("error.connection"));
        }
    }
}