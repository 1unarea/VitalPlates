package org.vitalplates.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.vitalplates.config.BackgroundIntensity;
import org.vitalplates.config.TransitionMode;

import static org.junit.jupiter.api.Assertions.*;

public class ColorGradientCalculatorTest {

    @Test
    @DisplayName("Classic Mode: 100% health produces pure White (#FFFFFF)")
    void testClassicFullHealthWhite() {
        int color = ColorGradientCalculator.getTextColorRgb(1.0f, TransitionMode.CLASSIC);
        assertEquals(0xFFFFFF, color, "100% health in Classic mode must produce #FFFFFF");
        assertEquals("#FFFFFF", ColorGradientCalculator.toHexString(color));
    }

    @Test
    @DisplayName("Classic Mode: 0% health produces Minecraft classic &c Red (#FF5555)")
    void testClassicZeroHealthClassicRed() {
        int color = ColorGradientCalculator.getTextColorRgb(0.0f, TransitionMode.CLASSIC);
        assertEquals(0xFF5555, color, "0% health in Classic mode must produce #FF5555");
        assertEquals("#FF5555", ColorGradientCalculator.toHexString(color));
    }

    @Test
    @DisplayName("Classic Mode: 50% health produces soft pastel reddish-white (#FFAAAA)")
    void testClassicHalfHealthMidpoint() {
        int color = ColorGradientCalculator.getTextColorRgb(0.5f, TransitionMode.CLASSIC);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        assertEquals(255, r, "Red channel must remain 255");
        assertEquals(170, g, "Green channel must be 170 at 50%");
        assertEquals(170, b, "Blue channel must be 170 at 50%");
        assertEquals("#FFAAAA", ColorGradientCalculator.toHexString(color));
    }

    @Test
    @DisplayName("Spectrum Mode: 100% produces green (#00FF00) and 50% produces yellow (#FFFF00)")
    void testSpectrumModeColors() {
        int full = ColorGradientCalculator.getTextColorRgb(1.0f, TransitionMode.SPECTRUM);
        assertEquals(0x00FF00, full, "100% in Spectrum mode must be #00FF00");

        int half = ColorGradientCalculator.getTextColorRgb(0.5f, TransitionMode.SPECTRUM);
        assertEquals(0xFFFF00, half, "50% in Spectrum mode must be #FFFF00");

        int quarter = ColorGradientCalculator.getTextColorRgb(0.25f, TransitionMode.SPECTRUM);
        assertEquals(0xFF8000, quarter, "25% in Spectrum mode must be #FF8000");

        int zero = ColorGradientCalculator.getTextColorRgb(0.0f, TransitionMode.SPECTRUM);
        assertEquals(0xFF5555, zero, "0% in Spectrum mode must be #FF5555");
    }

    @Test
    @DisplayName("Plate Background: Full health produces standard black plate")
    void testPlateBackgroundAtFullHealth() {
        int bg = ColorGradientCalculator.getPlateBackgroundColor(0.25f, 1.0f, true, BackgroundIntensity.NORMAL);
        int r = (bg >> 16) & 0xFF;
        int g = (bg >> 8) & 0xFF;
        int b = bg & 0xFF;

        assertEquals(0, r, "Plate red must be 0 at full health");
        assertEquals(0, g, "Plate green must be 0 at full health");
        assertEquals(0, b, "Plate blue must be 0 at full health");
    }

    @Test
    @DisplayName("Plate Background: Low health reddens the plate ('hafif kızarsın')")
    void testPlateBackgroundAtLowHealth() {
        int bg = ColorGradientCalculator.getPlateBackgroundColor(0.25f, 0.0f, true, BackgroundIntensity.NORMAL);
        int a = (bg >> 24) & 0xFF;
        int r = (bg >> 16) & 0xFF;
        int g = (bg >> 8) & 0xFF;
        int b = bg & 0xFF;

        assertTrue(r > 100, "Plate red channel must be tinted red at 0% health, got: " + r);
        assertTrue(r > g && r > b, "Plate red must be significantly higher than green and blue");
        assertTrue(a >= (int)(0.25f * 255), "Alpha must remain at or slightly above base opacity");
    }

    @Test
    @DisplayName("Plate Background: Disabled tinting returns untinted black plate")
    void testPlateBackgroundDisabled() {
        int bg = ColorGradientCalculator.getPlateBackgroundColor(0.25f, 0.0f, false, BackgroundIntensity.NORMAL);
        int r = (bg >> 16) & 0xFF;
        int g = (bg >> 8) & 0xFF;
        int b = bg & 0xFF;

        assertEquals(0, r, "Plate must remain pure black when tinting is disabled");
        assertEquals(0, g, "Plate must remain pure black when tinting is disabled");
        assertEquals(0, b, "Plate must remain pure black when tinting is disabled");
    }

    @ParameterizedTest
    @ValueSource(floats = {-0.1f, -5.0f, -100.0f})
    @DisplayName("Negative health safely clamps without errors")
    void testNegativeHealth(float health) {
        float ratio = ColorGradientCalculator.getHealthRatio(health, 20.0f, 0.0f);
        assertEquals(0.0f, ratio);
        int color = ColorGradientCalculator.getTextColorRgb(ratio, TransitionMode.CLASSIC);
        assertEquals(0xFF5555, color, "Negative health must produce &c red in classic mode");
    }

    @Test
    @DisplayName("Health exceeding max health (absorption/buffs) clamps to 1.0 safely")
    void testOverMaxHealth() {
        float ratio = ColorGradientCalculator.getHealthRatio(26.0f, 20.0f, 0.0f);
        assertEquals(1.0f, ratio, "Health > maxHealth must clamp to 1.0");

        float ratioAbs = ColorGradientCalculator.getHealthRatio(20.0f, 20.0f, 4.0f);
        assertEquals(1.0f, ratioAbs, "Health + absorption > maxHealth must clamp to 1.0");
    }

    @Test
    @DisplayName("NaN and Infinity handled defensively without exceptions")
    void testNaNAndInfinity() {
        assertDoesNotThrow(() -> {
            float r1 = ColorGradientCalculator.getHealthRatio(Float.NaN, 20.0f, 0.0f);
            float r2 = ColorGradientCalculator.getHealthRatio(10.0f, Float.NaN, 0.0f);
            float r3 = ColorGradientCalculator.getHealthRatio(10.0f, 20.0f, Float.NaN);
            float r4 = ColorGradientCalculator.getHealthRatio(Float.POSITIVE_INFINITY, 20.0f, 0.0f);
            float r5 = ColorGradientCalculator.getHealthRatio(Float.NEGATIVE_INFINITY, 20.0f, 0.0f);
            float r6 = ColorGradientCalculator.getHealthRatio(20.0f, Float.POSITIVE_INFINITY, 0.0f);

            assertTrue(r1 >= 0.0f && r1 <= 1.0f);
            assertTrue(r2 >= 0.0f && r2 <= 1.0f);
            assertTrue(r3 >= 0.0f && r3 <= 1.0f);
            assertTrue(r4 >= 0.0f && r4 <= 1.0f);
            assertTrue(r5 >= 0.0f && r5 <= 1.0f);
            assertTrue(r6 >= 0.0f && r6 <= 1.0f);
        });
    }

    @Test
    @DisplayName("Zero or negative max health handled safely without division by zero")
    void testZeroOrNegativeMaxHealth() {
        assertDoesNotThrow(() -> {
            float r1 = ColorGradientCalculator.getHealthRatio(10.0f, 0.0f, 0.0f);
            float r2 = ColorGradientCalculator.getHealthRatio(10.0f, -10.0f, 0.0f);
            assertTrue(r1 >= 0.0f && r1 <= 1.0f);
            assertTrue(r2 >= 0.0f && r2 <= 1.0f);
        });
    }
}
