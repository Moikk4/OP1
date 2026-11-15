package org.example;

import javafx.application.Application;

import java.sql.Connection;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Entry point. With "--cli" it runs a console demo (used to verify the
 * Docker image without a display); otherwise it launches the JavaFX GUI.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equals(args[0])) {
            runCliDemo(args);
        } else {
            Application.launch(TemperatureConverterGUI.class, args);
        }
    }

    /**
     * Console demo: prompt for language, convert a few temperatures, and persist them.
     */
    public static void runCliDemo(String[] args) {
        ResourceBundle messages = loadMessages(selectLocale(args));
        TempCalculator calc = new TempCalculator();
        System.out.println(messages.getString("app.title"));
        System.out.printf(messages.getString("result.celsius.to.fahrenheit") + "%n", 100.0, calc.celsiusToFahrenheit(100));
        System.out.printf(messages.getString("result.fahrenheit.to.celsius") + "%n", 32.0, calc.fahrenheitToCelsius(32));
        System.out.printf(messages.getString("result.kelvin.to.celsius") + "%n", 300.0, calc.kelvinToCelsius(300));
        System.out.printf(messages.getString("result.extreme") + "%n", 51.0, localizedBoolean(calc.isExtremeTemperature(51), messages));

        try {
            DBConnection.initializeDatabase();
            Connection conn = DBConnection.getConnection();
            new TemperatureUnitDAO(conn).seedDefaults();
            TempRecordDAO dao = new TempRecordDAO(conn);
            dao.save(new TempRecord(100, TemperatureUnit.CELSIUS,
                    TemperatureUnit.FAHRENHEIT, calc.celsiusToFahrenheit(100)));
            System.out.println(messages.getString("db.saved.records") + " " + dao.count());
            dao.findAll().forEach(r -> System.out.println("  " + r));
        } catch (Exception e) {
            System.out.println(messages.getString("db.unavailable") + " " + e.getMessage());
        }
    }

    private static Locale selectLocale(String[] args) {
        String selection = args.length > 1 ? args[1] : promptForLanguage();
        return switch (selection.trim().toLowerCase(Locale.ROOT)) {
            case "2", "fi", "fi_fi", "finnish", "suomi" -> Locale.forLanguageTag("fi-FI");
            case "3", "sv", "sv_se", "swedish", "svenska" -> Locale.forLanguageTag("sv-SE");
            case "4", "ja", "ja_jp", "japanese", "nihongo" -> Locale.forLanguageTag("ja-JP");
            case "5", "ar", "arabic", "عربي" -> Locale.forLanguageTag("ar");
            default -> Locale.US;
        };
    }

    private static String promptForLanguage() {
        System.out.println("Select language:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Swedish");
        System.out.println("4. Japanese");
        System.out.println("5. Arabic");
        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        return scanner.hasNextLine() ? scanner.nextLine() : "1";
    }

    private static ResourceBundle loadMessages(Locale locale) {
        try {
            return ResourceBundle.getBundle("MessagesBundle", locale);
        } catch (MissingResourceException e) {
            return ResourceBundle.getBundle("MessagesBundle", Locale.US);
        }
    }

    private static String localizedBoolean(boolean value, ResourceBundle messages) {
        return messages.getString(value ? "boolean.true" : "boolean.false");
    }
}

