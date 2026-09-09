package com.aeroport.viewmodel.commands.main;

import com.aeroport.viewmodel.MainContainerViewModel;
import com.aeroport.viewmodel.commands.ICommand;

public class LogoutCommand implements ICommand {
    private final MainContainerViewModel viewModel;

    public LogoutCommand(MainContainerViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void execute() {
        // Comanda doar dă ordine, ViewModel-ul execută modificarea stării
        viewModel.reseteazaDateSesiune();
        viewModel.incarcaEcranInitial();
    }
}