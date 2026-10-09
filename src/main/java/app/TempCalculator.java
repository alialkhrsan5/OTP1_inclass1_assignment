package app;
public class TempCalculator {

    public static void validateCelsius(double celsius) {
        if (celsius < -273.15) {
            throw new IllegalArgumentException("Celsius cannot be below absolute zero: " + celsius);
        }
    }

    public static void validateKelvin(double kelvin) {
        if (kelvin < 0) {
            throw new IllegalArgumentException("Kelvin cannot be negative: " + kelvin);
        }
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    public static double celsiusToFahrenheit(double celsius) {
        validateCelsius(celsius);
        return (celsius * 9 / 5) + 32;
    }

    public static double kelvinToCelsius(double kelvin) {
        validateKelvin(kelvin);
        return kelvin - 273.15;
    }

    public static boolean isExtremeTemperature(double celsius) {
        return celsius < -40 || celsius > 50;
    }

    public static double convert(String unitName, double value) {
        switch (unitName) {
            case "Fahrenheit to Celsius":
                return fahrenheitToCelsius(value);
            case "Celsius to Fahrenheit":
                return celsiusToFahrenheit(value);
            case "Kelvin to Celsius":
                return kelvinToCelsius(value);
            default:
                throw new IllegalArgumentException("Unknown conversion: " + unitName);
        }
    }
}
