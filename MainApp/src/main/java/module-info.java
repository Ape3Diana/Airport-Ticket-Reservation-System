module com.aeroport.client {
    // Librăriile de care avem nevoie
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.net.http;

    // 1. Permite JavaFX să ruleze clasa MainApp (Eroarea ta de acum)
    exports com.aeroport;

    // 2. Permite JavaFX să acceseze controllerele din pachetul view
    // (Necesar pentru @FXML și inițializarea ferestrelor)
    opens com.aeroport.view to javafx.fxml;

    // 3. Permite Jackson să citească datele în GenericDataModel
    // (Altfel o să ai erori când primești date de la server)
    opens com.aeroport.model to com.fasterxml.jackson.databind;

    // Exportăm și restul pachetelor pentru a fi vizibile în interiorul proiectului
    exports com.aeroport.view;
    exports com.aeroport.viewmodel;
    exports com.aeroport.model;
    exports com.aeroport.viewmodel.utils;
    opens com.aeroport.viewmodel.utils to com.fasterxml.jackson.databind;
}