package org.vitalplates.util;

import net.minecraft.util.ARGB;
import org.vitalplates.config.BackgroundIntensity;
import org.vitalplates.config.TransitionMode;

/**
 * Clean-room color and gradient calculator for VitalPlates nametag rendering.
 * <p>
 * Supports:
 * <ul>
 *   <li>Classic mode (Default): Smooth linear shift from White (#FFFFFF) at 100% health
 *       directly to classic Minecraft &c Red (#FF5555) at 0% health, without intermediate hues.</li>
 *   <li>Spectrum mode: Multi-stop gradient transitioning from Green (#00FF00) through
 *       Yellow (#FFFF00) and Orange (#FF8000) down to Red (#FF5555).</li>
 *   <li>Background Plate tinting: Subtly reddens the translucent nametag background plate
 *       as entity health decreases ("hafif kızarsın").</li>
 * </ul>
 */
public final class ColorGradientCalculator {

    // Minecraft &c Classic Red (HEX: #FF5555, RGB: 255, 85, 85)
    public static final int COLOR_CLASSIC_RED = 0xFF5555;
    // Pure White (HEX: #FFFFFF, RGB: 255, 255, 255)
    public static final int COLOR_CLASSIC_WHITE = 0xFFFFFF;

    // Spectrum mode colors
    public static final int COLOR_SPECTRUM_GREEN = 0x00FF00;
    public static final int COLOR_SPECTRUM_YELLOW = 0xFFFF00;
    public static final int COLOR_SPECTRUM_ORANGE = 0xFF8000;

    private ColorGradientCalculator() {
        // Utility class
    }

    /**
     * Calculates the normalized health ratio [0.0, 1.0] from current health, max health,
     * and absorption hearts, handling all boundary and edge cases defensively.
     */
    public static float getHealthRatio(float health, float maxHealth, float absorption) {
        if (Float.isNaN(health) || Float.isNaN(maxHealth) || Float.isNaN(absorption)
                || Float.isInfinite(maxHealth) || maxHealth <= 0.0f) {
            return (health <= 0.0f && !Float.isNaN(health)) ? 0.0f : 1.0f;
        }

        if (Float.isInfinite(health)) {
            return health > 0 ? 1.0f : 0.0f;
        }

        float validAbsorption = Float.isInfinite(absorption) ? 0.0f : Math.max(0.0f, absorption);
        float effectiveHealth = health + validAbsorption;

        if (effectiveHealth <= 0.0f) {
            return 0.0f;
        }

        float ratio = effectiveHealth / maxHealth;
        return Math.clamp(ratio, 0.0f, 1.0f);
    }

    /**
     * Calculates the text RGB color for the given health ratio and transition mode.
     *
     * @param ratio normalized health ratio in [0.0, 1.0]
     * @param mode  transition mode (CLASSIC or SPECTRUM)
     * @return 24-bit RGB integer (0xRRGGBB)
     */
    public static int getTextColorRgb(float ratio, TransitionMode mode) {
        ratio = Math.clamp(ratio, 0.0f, 1.0f);

        if (mode == TransitionMode.CLASSIC) {
            // Classic: White (#FFFFFF: 255, 255, 255) -> &c Red (#FF5555: 255, 85, 85)
            // R remains constant 255. G and B smoothly interpolate from 255 down to 85.
            int r = 255;
            int g = Math.round(85.0f + (170.0f * ratio));
            int b = Math.round(85.0f + (170.0f * ratio));

            g = Math.clamp(g, 85, 255);
            b = Math.clamp(b, 85, 255);
            return (r << 16) | (g << 8) | b;
        } else {
            // Spectrum: Green (#00FF00) -> Yellow (#FFFF00) -> Orange (#FF8000) -> Red (#FF5555)
            if (ratio >= 0.5f) {
                // [0.5, 1.0]: Green -> Yellow
                float t = (ratio - 0.5f) * 2.0f;
                int r = Math.round(255.0f * (1.0f - t));
                int g = 255;
                int b = 0;
                return (Math.clamp(r, 0, 255) << 16) | (g << 8) | b;
            } else if (ratio >= 0.25f) {
                // [0.25, 0.5]: Yellow -> Orange
                float t = (ratio - 0.25f) * 4.0f; // 1 at 0.5, 0 at 0.25
                int r = 255;
                int g = Math.round(128.0f + (127.0f * t)); // 128 -> 255
                int b = 0;
                return (r << 16) | (Math.clamp(g, 128, 255) << 8) | b;
            } else {
                // [0.0, 0.25]: Orange -> Classic Red (#FF5555)
                float t = ratio * 4.0f; // 1 at 0.25, 0 at 0.0
                int r = 255;
                int g = Math.round(85.0f + (43.0f * t)); // 85 -> 128
                int b = Math.round(85.0f * (1.0f - t));  // 85 -> 0
                return (r << 16) | (Math.clamp(g, 85, 128) << 8) | Math.clamp(b, 0, 85);
            }
        }
    }

    /**
     * Backward-compatible helper method using the active transition mode.
     */
    public static int getHealthColorRgb(float health, float maxHealth, float absorption) {
        float ratio = getHealthRatio(health, maxHealth, absorption);
        return getTextColorRgb(ratio, TransitionMode.CLASSIC);
    }

    /**
     * Calculates the nametag background plate color, subtly shifting from standard
     * translucent black at full health to a soft translucent red at low health.
     *
     * @param baseOpacity     vanilla background opacity (default ~0.25f)
     * @param ratio           health ratio in [0.0, 1.0]
     * @param tintBackground  whether background tinting is enabled
     * @param intensity       intensity setting for background reddening
     * @return 32-bit ARGB integer for the background plate
     */
    public static int getPlateBackgroundColor(float baseOpacity, float ratio, boolean tintBackground, BackgroundIntensity intensity) {
        if (!tintBackground) {
            return ARGB.color(baseOpacity, 0x000000);
        }

        ratio = Math.clamp(ratio, 0.0f, 1.0f);
        float factor = intensity != null ? intensity.getFactor() : BackgroundIntensity.NORMAL.getFactor();
        float damageFactor = (1.0f - ratio) * factor;

        // Base plate RGB tints towards a soft dark red (#B42020)
        int plateR = Math.round(180.0f * damageFactor);
        int plateG = Math.round(32.0f * damageFactor);
        int plateB = Math.round(32.0f * damageFactor);

        plateR = Math.clamp(plateR, 0, 255);
        plateG = Math.clamp(plateG, 0, 255);
        plateB = Math.clamp(plateB, 0, 255);
        int plateRgb = (plateR << 16) | (plateG << 8) | plateB;

        // Slightly increase alpha as health drops so the soft red is comfortably visible
        float effectiveAlpha = Math.clamp(baseOpacity + (0.12f * damageFactor), 0.0f, 1.0f);
        return ARGB.color(effectiveAlpha, plateRgb);
    }

    /**
     * Helper to format a 24-bit RGB color integer as a 6-digit hex string (#RRGGBB).
     */
    public static String toHexString(int rgb) {
        return String.format("#%06X", rgb & 0xFFFFFF);
    }
}
