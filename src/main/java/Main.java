public class Main {
    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();

        System.out.println("=== Temperature Converter (Ali) ===");
        System.out.println("100 F = " + converter.fahrenheitToCelsius(100) + " C");
        System.out.println("37 C  = " + converter.celsiusToFahrenheit(37) + " F");
        System.out.println("300 K = " + converter.kelvinToCelsius(300) + " C");
        System.out.println("Is -50 C extreme? " + converter.isExtremeTemperature(-50));
        System.out.println("Is 25 C extreme?  " + converter.isExtremeTemperature(25));
    }
}