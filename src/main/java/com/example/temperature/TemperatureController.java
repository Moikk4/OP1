package com.example.temperature;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class TemperatureController implements Initializable {
    @FXML private ComboBox<String> languageCombo;
    @FXML private ComboBox<UnitOption> fromUnitCombo;
    @FXML private ComboBox<UnitOption> toUnitCombo;
    @FXML private TextField inputField;
    @FXML private Button convertButton;
    @FXML private Label resultLabel;
    @FXML private ListView<String> historyList;

    private Locale currentLocale = Locale.ENGLISH;
    private final UnitDao unitDao = new UnitDao();
    private final ConversionService conversionService = new ConversionService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageCombo.setItems(FXCollections.observableArrayList("English", "Finnish", "Swedish", "Japanese", "Arabic"));
        languageCombo.getSelectionModel().selectFirst();

        languageCombo.valueProperty().addListener((obs, oldLang, newLang) -> {
            currentLocale = switch (newLang) {
                case "Finnish" -> new Locale("fi");
                case "Swedish" -> new Locale("sv");
                case "Japanese" -> Locale.JAPANESE;
                case "Arabic" -> new Locale("ar");
                default -> Locale.ENGLISH;
            };
            applyLocale(currentLocale);
            loadUnits();
        });

        convertButton.setOnAction(e -> convertTemperature());

        loadUnits();
    }

    private void applyLocale(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.MessagesBundle", locale);
        resultLabel.setText(bundle.getString("app.result"));
        convertButton.setText(bundle.getString("button.convert"));
        languageCombo.setPromptText(bundle.getString("label.language"));

        if (locale.getLanguage().equals("ar")) {
            resultLabel.getScene().getRoot().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            resultLabel.getScene().getRoot().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private void loadUnits() {
        try {
            List<UnitOption> options = unitDao.findByCategory("temperature", currentLocale.toLanguageTag());
            fromUnitCombo.setItems(FXCollections.observableArrayList(options));
            toUnitCombo.setItems(FXCollections.observableArrayList(options));
            fromUnitCombo.getSelectionModel().selectFirst();
            toUnitCombo.getSelectionModel().selectLast();
        } catch (SQLException e) {
            showError("Database error");
        }
    }

    private void convertTemperature() {
        try {
            double value = Double.parseDouble(inputField.getText());
            UnitOption from = fromUnitCombo.getValue();
            UnitOption to = toUnitCombo.getValue();

            double converted = conversionService.toTarget(value, from.getCode(), to.getCode());
            resultLabel.setText(from.getName() + " -> " + to.getName() + ": " + converted);

            unitDao.saveConversion(value, from.getCode(), to.getCode(), converted, currentLocale.toLanguageTag());
            refreshHistory();
        } catch (Exception ex) {
            showError("Invalid input");
        }
    }

    private void refreshHistory() {
        try {
            historyList.setItems(FXCollections.observableArrayList(unitDao.findLastFive(currentLocale.toLanguageTag())));
        } catch (SQLException e) {
            showError("Error loading history");
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }
}

