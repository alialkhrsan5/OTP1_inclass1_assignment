import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    private TemperatureConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TemperatureConverter();
    }

    @Test
    void fahrenheitToCelsiusConvertsCorrectly() {
        assertEquals(0.0, converter.fahrenheitToCelsius(32), 0.0001);
        assertEquals(100.0, converter.fahrenheitToCelsius(212), 0.0001);
        assertEquals(-40.0, converter.fahrenheitToCelsius(-40), 0.0001);
        assertEquals(37.0, converter.fahrenheitToCelsius(98.6), 0.0001);
    }

    @Test
    void celsiusToFahrenheitConvertsCorrectly() {
        assertEquals(32.0, converter.celsiusToFahrenheit(0), 0.0001);
        assertEquals(212.0, converter.celsiusToFahrenheit(100), 0.0001);
        assertEquals(-40.0, converter.celsiusToFahrenheit(-40), 0.0001);
        assertEquals(98.6, converter.celsiusToFahrenheit(37), 0.0001);
    }

    @Test
    void extremeColdIsDetected() {
        assertTrue(converter.isExtremeTemperature(-41));
        assertTrue(converter.isExtremeTemperature(-100));
    }

    @Test
    void extremeHeatIsDetected() {
        assertTrue(converter.isExtremeTemperature(51));
        assertTrue(converter.isExtremeTemperature(120));
    }

    @Test
    void normalTemperaturesAreNotExtreme() {
        assertFalse(converter.isExtremeTemperature(0));
        assertFalse(converter.isExtremeTemperature(25));
        assertFalse(converter.isExtremeTemperature(-40));
        assertFalse(converter.isExtremeTemperature(50));
    }
}