package org.example;

import javafx.application.Application;

import java.sql.Connection;

/**
 * Entry point. With "--cli" it runs a console demo (used to verify the
 * Docker image without a display); otherwise it launches the JavaFX GUI.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equals(args[0])) {
            runCliDemo();
        } else {
            Application.launch(TemperatureConverterGUI.class, args);
        }
    }

    /**
     * Console demo: convert a few temperatures and persist them.
     */
    public static void runCliDemo() {
        TempCalculator calc = new TempCalculator();
        System.out.println("=== Temperature Converter (CLI) ===");
        System.out.printf("100 C = %.2f F%n", calc.celsiusToFahrenheit(100));
        System.out.printf("32 F  = %.2f C%n", calc.fahrenheitToCelsius(32));
        System.out.printf("300 K = %.2f C%n", calc.kelvinToCelsius(300));

        try {
            DBConnection.initializeDatabase();
            Connection conn = DBConnection.getConnection();
            new TemperatureUnitDAO(conn).seedDefaults();
            TempRecordDAO dao = new TempRecordDAO(conn);
            dao.save(new TempRecord(100, TemperatureUnit.CELSIUS,
                    TemperatureUnit.FAHRENHEIT, calc.celsiusToFahrenheit(100)));
            System.out.println("Saved records: " + dao.count());
            dao.findAll().forEach(r -> System.out.println("  " + r));
        } catch (Exception e) {
            System.out.println("Database not available: " + e.getMessage());
        }
    }
}

