package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * JavaFX temperature converter GUI with FXML and localization support.
 * Converts between Celsius, Fahrenheit and Kelvin with multilingual interface.
 */
public class TemperatureConverterGUI extends Application {

    private static Locale currentLocale = Locale.US;
    private static Stage primaryStage;
    private static TemperatureConverterController controller;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        loadAndShowGUI();
    }

    private void loadAndShowGUI() throws IOException {
        ResourceBundle messages = loadMessages(currentLocale);
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/temperature_converter.fxml"),
            messages
        );

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root, 500, 480);
        primaryStage.setTitle(messages.getString("gui.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Changes the application locale and reloads the GUI.
     */
    public static void setLocale(Locale newLocale) {
        currentLocale = newLocale;
        try {
            ResourceBundle messages = loadMessages(currentLocale);
            FXMLLoader loader = new FXMLLoader(
                TemperatureConverterGUI.class.getResource("/temperature_converter.fxml"),
                messages
            );

            Parent root = loader.load();
            controller = loader.getController();

            Scene scene = new Scene(root, 500, 480);
            primaryStage.setTitle(messages.getString("gui.title"));
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Failed to reload GUI: " + e.getMessage());
        }
    }

    private static ResourceBundle loadMessages(Locale locale) {
        try {
            return ResourceBundle.getBundle("MessagesBundle", locale);
        } catch (MissingResourceException e) {
            return ResourceBundle.getBundle("MessagesBundle", Locale.US);
        }
    }
}

