package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TempCalculatorTest {

    @Test
    public void fahrenheitToCelsiusConvertsCorrectly() {
        assertEquals(0.0, TempCalculator.fahrenheitToCelsius(32), 0.0001);
        assertEquals(100.0, TempCalculator.fahrenheitToCelsius(212), 0.0001);
        assertEquals(-40.0, TempCalculator.fahrenheitToCelsius(-40), 0.0001);
        assertEquals(37.0, TempCalculator.fahrenheitToCelsius(98.6), 0.0001);
    }

    @Test
    public void celsiusToFahrenheitConvertsCorrectly() {
        assertEquals(32.0, TempCalculator.celsiusToFahrenheit(0), 0.0001);
        assertEquals(212.0, TempCalculator.celsiusToFahrenheit(100), 0.0001);
        assertEquals(-40.0, TempCalculator.celsiusToFahrenheit(-40), 0.0001);
        assertEquals(98.6, TempCalculator.celsiusToFahrenheit(37), 0.0001);
    }

    @Test
    public void kelvinToCelsiusConvertsCorrectly() {
        assertEquals(26.85, TempCalculator.kelvinToCelsius(300), 0.0001);
        assertEquals(0.0, TempCalculator.kelvinToCelsius(273.15), 0.0001);
        assertEquals(-273.15, TempCalculator.kelvinToCelsius(0), 0.0001);
        assertEquals(100.0, TempCalculator.kelvinToCelsius(373.15), 0.0001);
    }

    @Test
    public void extremeColdIsDetected() {
        assertTrue(TempCalculator.isExtremeTemperature(-41));
        assertTrue(TempCalculator.isExtremeTemperature(-100));
    }

    @Test
    public void extremeHeatIsDetected() {
        assertTrue(TempCalculator.isExtremeTemperature(51));
        assertTrue(TempCalculator.isExtremeTemperature(120));
    }

    @Test
    public void normalTemperaturesAreNotExtreme() {
        assertFalse(TempCalculator.isExtremeTemperature(0));
        assertFalse(TempCalculator.isExtremeTemperature(25));
        assertFalse(TempCalculator.isExtremeTemperature(-40));
        assertFalse(TempCalculator.isExtremeTemperature(50));
    }

    @Test
    public void validateCelsiusAcceptsValidValue() {
        assertDoesNotThrow(() -> TempCalculator.validateCelsius(-273.15));
        assertDoesNotThrow(() -> TempCalculator.validateCelsius(20));
    }

    @Test
    public void validateCelsiusRejectsBelowAbsoluteZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> TempCalculator.validateCelsius(-300));
        assertTrue(ex.getMessage().contains("absolute zero"));
    }

    @Test
    public void validateKelvinRejectsNegativeValue() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> TempCalculator.validateKelvin(-1));
        assertTrue(ex.getMessage().contains("Kelvin cannot be negative"));
    }

    @Test
    public void convertUsesTheSelectedConversionType() {
        assertEquals(0.0, TempCalculator.convert("Fahrenheit to Celsius", 32), 0.0001);
        assertEquals(32.0, TempCalculator.convert("Celsius to Fahrenheit", 0), 0.0001);
        assertEquals(26.85, TempCalculator.convert("Kelvin to Celsius", 300), 0.0001);
    }

    @Test
    public void convertRejectsUnknownConversionType() {
        assertThrows(IllegalArgumentException.class,
                () -> TempCalculator.convert("Celsius to Kelvin", 10));
    }
}
