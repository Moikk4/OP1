public class TemperatureConverter {

    public double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public boolean isExtremeTemperature(double celsius) {
        return celsius < -40.0 || celsius > 50.0;
    }

    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();

        System.out.println("=== Temperature Converter ===");
        System.out.printf("32 F  = %.2f C%n", converter.fahrenheitToCelsius(32.0));
        System.out.printf("100 C = %.2f F%n", converter.celsiusToFahrenheit(100.0));
        System.out.printf("300 K = %.2f C%n", converter.kelvinToCelsius(300.0));
        System.out.println("Is 60 C extreme? " + converter.isExtremeTemperature(60.0));
        System.out.println("Is 20 C extreme? " + converter.isExtremeTemperature(20.0));
    }
}
