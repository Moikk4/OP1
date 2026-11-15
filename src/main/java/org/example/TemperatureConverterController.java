package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * FXML Controller for the temperature converter GUI.
 * Implements DB-driven localization with English fallback and RTL support for Arabic.
 */
public class TemperatureConverterController implements Initializable {

    @FXML private Label titleLabel;
    @FXML private Label languageLabel;
    @FXML private Label resultLabel;
    @FXML private Label historyLabel;
    @FXML private TextField inputField;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<LocalizedUnit> fromBox;
    @FXML private ComboBox<LocalizedUnit> toBox;
    @FXML private Button convertButton;
    @FXML private ListView<String> historyList;

    private final TempCalculator calculator = new TempCalculator();
    private TempRecordDAO recordDAO;
    private LocalizedUnitDAO localizedUnitDAO;
    private ResourceBundle messages;
    private Locale currentLocale = Locale.US;
    private String currentLanguageCode = "en";
    private VBox rootNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.messages = resources;
        initDatabase();
        setupLanguageComboBox();
        loadLocalizedUnits();
        refreshHistory();
    }

    private void initDatabase() {
        try {
            DBConnection.initializeDatabase();
            Connection conn = DBConnection.getConnection();
            recordDAO = new TempRecordDAO(conn);
            localizedUnitDAO = new LocalizedUnitDAO(conn);
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            recordDAO = null;
            localizedUnitDAO = null;
        }
    }

    private void setupLanguageComboBox() {
        languageComboBox.getItems().addAll(
            messages.getString("language.english"),
            messages.getString("language.finnish"),
            messages.getString("language.swedish"),
            messages.getString("language.japanese"),
            messages.getString("language.arabic")
        );
        languageComboBox.setValue(messages.getString("language.english"));
        currentLanguageCode = "en";
        applyLanguageDirection();
    }

    /**
     * Load temperature units from database with localization.
     * This is the key feature: unit names come from the DB, not from .properties files.
     */
    private void loadLocalizedUnits() {
        if (localizedUnitDAO == null) {
            return;
        }

        try {
            List<LocalizedUnit> units = localizedUnitDAO.getTemperatureUnits(currentLanguageCode);

            fromBox.getItems().clear();
            toBox.getItems().clear();

            fromBox.getItems().addAll(units);
            toBox.getItems().addAll(units);

            // Set defaults to Celsius and Fahrenheit
            if (!units.isEmpty()) {
                fromBox.setValue(units.stream()
                    .filter(u -> u.getUnitKey().equals("CELSIUS"))
                    .findFirst()
                    .orElse(units.get(0)));

                toBox.setValue(units.stream()
                    .filter(u -> u.getUnitKey().equals("FAHRENHEIT"))
                    .findFirst()
                    .orElse(units.size() > 1 ? units.get(1) : units.get(0)));
            }
        } catch (Exception e) {
            System.err.println("Failed to load localized units: " + e.getMessage());
        }
    }

    @FXML
    private void onLanguageChange() {
        String selected = languageComboBox.getValue();

        if (selected == null) return;

        Locale newLocale = switch (selected) {
            case "English", "Englanti", "Engelska", "英語", "الإنجليزية" -> Locale.US;
            case "Finnish", "Suomi", "Finska", "フィンランド語", "الفنلندية" -> Locale.forLanguageTag("fi-FI");
            case "Swedish", "Svenska", "Ruotsi", "スウェーデン語", "السويدية" -> Locale.forLanguageTag("sv-SE");
            case "Japanese", "Japani", "Japanska", "日本語", "اليابانية" -> Locale.forLanguageTag("ja-JP");
            case "Arabic", "Arabia", "Arabiska", "アラビア語", "العربية" -> Locale.forLanguageTag("ar");
            default -> Locale.US;
        };

        if (!newLocale.equals(currentLocale)) {
            currentLocale = newLocale;
            currentLanguageCode = LocalizedUnitDAO.normalizeLanguageCode(currentLocale.toLanguageTag());
            applyLanguageDirection();
            TemperatureConverterGUI.setLocale(currentLocale);
        }
    }

    private void applyLanguageDirection() {
        if (languageComboBox == null || languageComboBox.getScene() == null || languageComboBox.getScene().getRoot() == null) {
            return;
        }

        NodeOrientation orientation = "ar".equals(currentLanguageCode)
                ? NodeOrientation.RIGHT_TO_LEFT
                : NodeOrientation.LEFT_TO_RIGHT;
        languageComboBox.getScene().getRoot().setNodeOrientation(orientation);
    }

    @FXML
    private void convert() {
        try {
            double value = Double.parseDouble(inputField.getText().trim().replace(',', '.'));
            LocalizedUnit fromUnit = fromBox.getValue();
            LocalizedUnit toUnit = toBox.getValue();
            if (fromUnit == null || toUnit == null) {
                resultLabel.setText(messages.getString("gui.result.invalid"));
                return;
            }

            TemperatureUnit from = TemperatureUnit.valueOf(fromUnit.getUnitKey());
            TemperatureUnit to = TemperatureUnit.valueOf(toUnit.getUnitKey());
            double result = calculator.convert(value, from, to);
            resultLabel.setText(String.format("%s %.2f %s",
                messages.getString("gui.result").replace("—", ""), result, to.getSymbol()));

            if (recordDAO != null) {
                recordDAO.save(new TempRecord(value, from, to, result), currentLanguageCode);
                refreshHistory();
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText(messages.getString("gui.result.invalid"));
        } catch (IllegalArgumentException ex) {
            resultLabel.setText(messages.getString("gui.result.invalid"));
        }
    }

    private void refreshHistory() {
        historyList.getItems().clear();
        if (recordDAO == null) {
            historyList.getItems().add(messages.getString("gui.history.unavailable"));
            return;
        }
        List<TempRecord> records = recordDAO.findLast(5);
        for (TempRecord r : records) {
            historyList.getItems().add(r.toString());
        }
    }

    /**
     * Allows external refresh after locale change.
     */
    public void refresh(ResourceBundle newMessages) {
        this.messages = newMessages;
        titleLabel.setText(messages.getString("gui.title"));
        languageLabel.setText(messages.getString("gui.language"));
        resultLabel.setText(messages.getString("gui.result"));
        historyLabel.setText(messages.getString("gui.history"));
        inputField.setPromptText(messages.getString("gui.input.prompt"));
        convertButton.setText(messages.getString("gui.convert.button"));

        String currentSelection = switch (currentLanguageCode) {
            case "fi" -> messages.getString("language.finnish");
            case "sv" -> messages.getString("language.swedish");
            case "ja" -> messages.getString("language.japanese");
            case "ar" -> messages.getString("language.arabic");
            default -> messages.getString("language.english");
        };

        languageComboBox.getItems().clear();
        languageComboBox.getItems().addAll(
            messages.getString("language.english"),
            messages.getString("language.finnish"),
            messages.getString("language.swedish"),
            messages.getString("language.japanese"),
            messages.getString("language.arabic")
        );
        languageComboBox.setValue(currentSelection);
        applyLanguageDirection();
        loadLocalizedUnits();
        refreshHistory();
    }

    private int getCurrentLanguageIndex() {
        String tag = currentLocale.toLanguageTag();
        return switch (tag) {
            case "en-US", "en" -> 0;
            case "fi-FI", "fi" -> 1;
            case "sv-SE", "sv" -> 2;
            case "ja-JP", "ja" -> 3;
            case "ar" -> 4;
            default -> 0;
        };
    }
}
