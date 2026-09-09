package com.aeroport.viewmodel.commands.main;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.aeroport.viewmodel.MainContainerViewModel;
import com.aeroport.viewmodel.commands.ICommand;
import java.util.Locale;

public class ChangeLanguageCommand implements ICommand {
    private final MainContainerViewModel viewModel;

    public ChangeLanguageCommand(MainContainerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    // FĂRĂ PARAMETRI!
    @Override
    public void execute() {
        // Citim limba direct din ViewModel
        String numeLimba = viewModel.limbaSelectataProperty().get();

        String cod = switch (numeLimba) {
            case "English" -> "en";
            case "Deutsch" -> "de";
            default -> "ro";
        };

        // 1. Schimbăm Locale-ul global
        LanguageManager.setLocale(new Locale(cod));

        // 2. Spunem ViewModel-ului să-și actualizeze textele traduse (Header)
        viewModel.actualizeazaTexteDupaLimba();

        // 3. Forțăm reîncărcarea ecranului curent pentru a aplica traducerile în FXML
        viewModel.reincarcaEcranCurent();
    }
}