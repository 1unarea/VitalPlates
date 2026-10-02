package org.vitalplates.config;

/**
 * Color transition modes supported by VitalPlates.
 */
public enum TransitionMode {
    /**
     * Classic mode: smooth linear transition from White (#FFFFFF) at full health
     * directly to classic Minecraft &c Red (#FF5555) at low health, with no intermediate colors.
     */
    CLASSIC("vitalplates.config.transition_mode.classic"),

    /**
     * Spectrum mode: multi-color gradient transitioning from Green (#00FF00)
     * through Yellow (#FFFF00) and Orange (#FF8000) down to Red (#FF5555).
     */
    SPECTRUM("vitalplates.config.transition_mode.spectrum");

    private final String translationKey;

    TransitionMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable(this.translationKey);
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
