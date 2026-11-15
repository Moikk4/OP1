package com.example.temperature;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TemperatureConverterApp extends Application {
    private static final String FXML = "/fxml/temperature-view.fxml";
    private static final String BUNDLE = "i18n.MessagesBundle";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Locale locale = Locale.ENGLISH;
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE, locale);
        Parent root = FXMLLoader.load(getClass().getResource(FXML), bundle);
        Scene scene = new Scene(root);
        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(scene);
        stage.show();
    }
}

