package com.example.temperature;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class TemperatureApp extends Application {
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    @Override
    public void start(Stage stage) throws Exception {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/temperature.fxml"), bundle);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

