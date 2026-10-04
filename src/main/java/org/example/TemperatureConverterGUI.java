package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

/**
 * JavaFX temperature converter GUI. Converts between Celsius, Fahrenheit
 * and Kelvin and stores every conversion in the database.
 */
public class TemperatureConverterGUI extends Application {

    private final TempCalculator calculator = new TempCalculator();
    private TempRecordDAO recordDAO;

    private TextField inputField;
    private ComboBox<TemperatureUnit> fromBox;
    private ComboBox<TemperatureUnit> toBox;
    private Label resultLabel;
    private ListView<String> historyList;

    @Override
    public void start(Stage stage) {
        initDatabase();

        Label title = new Label("Temperature Converter");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        inputField = new TextField();
        inputField.setPromptText("Enter value");
        inputField.setPrefWidth(120);

        fromBox = new ComboBox<>();
        fromBox.getItems().setAll(TemperatureUnit.values());
        fromBox.setValue(TemperatureUnit.CELSIUS);

        Label arrow = new Label("→");

        toBox = new ComboBox<>();
        toBox.getItems().setAll(TemperatureUnit.values());
        toBox.setValue(TemperatureUnit.FAHRENHEIT);

        Button convertBtn = new Button("Convert");
        convertBtn.setDefaultButton(true);
        convertBtn.setOnAction(e -> convert());

        HBox inputRow = new HBox(10, inputField, fromBox, arrow, toBox, convertBtn);
        inputRow.setAlignment(Pos.CENTER);

        resultLabel = new Label("Result: —");
        resultLabel.setStyle("-fx-font-size: 16px;");

        Label historyTitle = new Label("History");
        historyTitle.setStyle("-fx-font-weight: bold;");
        historyList = new ListView<>();
        historyList.setPrefHeight(180);
        refreshHistory();

        VBox root = new VBox(15, title, inputRow, resultLabel, historyTitle, historyList);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

        stage.setTitle("Temperature Converter");
        stage.setScene(new Scene(root, 480, 420));
        stage.show();
    }

    private void initDatabase() {
        try {
            DBConnection.initializeDatabase();
            Connection conn = DBConnection.getConnection();
            new TemperatureUnitDAO(conn).seedDefaults();
            recordDAO = new TempRecordDAO(conn);
        } catch (Exception e) {
            recordDAO = null; // DB unavailable – GUI still works, history disabled
        }
    }

    private void convert() {
        try {
            double value = Double.parseDouble(inputField.getText().trim().replace(',', '.'));
            TemperatureUnit from = fromBox.getValue();
            TemperatureUnit to = toBox.getValue();
            double result = calculator.convert(value, from, to);
            resultLabel.setText(String.format("Result: %.2f %s", result, to.getSymbol()));
            if (recordDAO != null) {
                recordDAO.save(new TempRecord(value, from, to, result));
                refreshHistory();
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Result: invalid number");
        }
    }

    private void refreshHistory() {
        historyList.getItems().clear();
        if (recordDAO == null) {
            historyList.getItems().add("(database unavailable)");
            return;
        }
        List<TempRecord> records = recordDAO.findAll();
        for (TempRecord r : records) {
            historyList.getItems().add(r.toString());
        }
    }
}

