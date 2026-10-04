package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureUnitTest {

    @Test
    public void constructorAndGettersWork() {
        TemperatureUnit unit = new TemperatureUnit(1, "Fahrenheit to Celsius");
        assertEquals(1, unit.getId());
        assertEquals("Fahrenheit to Celsius", unit.getUnitName());
    }

    @Test
    public void toStringReturnsUnitName() {
        TemperatureUnit unit = new TemperatureUnit(3, "Kelvin to Celsius");
        assertEquals("Kelvin to Celsius", unit.toString());
    }
}
