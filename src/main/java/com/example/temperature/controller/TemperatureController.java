package com.example.temperature.controller;

import com.example.temperature.dao.UnitDao;
import com.example.temperature.service.TemperatureConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class TemperatureController {
    @FXML private ComboBox<String> languageCombo;
    @FXML private ComboBox<String> fromUnitCombo;
    @FXML private ComboBox<String> toUnitCombo;
    @FXML private TextField inputValueField;
    @FXML private Button convertButton;
    @FXML private Label resultLabel;
    @FXML private ListView<String> historyListView;
    @FXML private Label titleLabel;

    private final UnitDao unitDao = new UnitDao();
    private Locale currentLocale = Locale.ENGLISH;

    @FXML
    public void initialize() {
        languageCombo.setItems(FXCollections.observableArrayList(
                "en", "fi", "sv", "ja", "ar"
        ));
        languageCombo.setValue("en");
        languageCombo.setOnAction(e -> changeLanguage(languageCombo.getValue()));

        loadUnitsForLanguage("en");
        updateHistory();
    }

    private void changeLanguage(String languageCode) {
        currentLocale = switch (languageCode) {
            case "fi" -> new Locale("fi");
            case "sv" -> new Locale("sv");
            case "ja" -> new Locale("ja");
            case "ar" -> Locale.forLanguageTag("ar");
            default -> Locale.ENGLISH;
        };

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", currentLocale);
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/temperature.fxml"), bundle);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            if ("ar".equals(languageCode)) {
                root.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
            } else {
                root.setNodeOrientation(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadUnitsForLanguage(String languageCode) {
        try {
            Map<String, String> units = unitDao.findUnitsByCategory("temperature", languageCode);
            List<String> names = new ArrayList<>(units.values());
            fromUnitCombo.setItems(FXCollections.observableArrayList(names));
            toUnitCombo.setItems(FXCollections.observableArrayList(names));
            fromUnitCombo.getSelectionModel().selectFirst();
            toUnitCombo.getSelectionModel().select(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void convertTemperature() {
        try {
            double input = Double.parseDouble(inputValueField.getText());
            String from = fromUnitCombo.getValue();
            String to = toUnitCombo.getValue();

            double result = TemperatureConverter.convert(input, from, to);
            resultLabel.setText(String.format(Locale.US, "%.2f %s -> %.2f %s",
                    input, from, result, to));
            saveHistory(input, from, to, result);
        } catch (Exception e) {
            resultLabel.setText("Invalid input");
        }
    }

    private void saveHistory(double input, String from, String to, double result) {
        // ...existing code...
    }

    private void updateHistory() {
        // ...existing code...
    }
}

