package com.aeroport;

import com.aeroport.viewmodel.utils.LanguageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ResourceBundle;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Încărcăm dicționarul de limbi
        ResourceBundle bundle = LanguageManager.getBundle();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainContainerView.fxml"), bundle);
        Parent root = loader.load();

        primaryStage.setTitle("Airport Management System");
        primaryStage.setScene(new Scene(root, 1100, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}