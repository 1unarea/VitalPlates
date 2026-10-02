package org.vitalplates.config;

/**
 * Intensity factor for the nametag plate background reddening.
 */
public enum BackgroundIntensity {
    LOW("vitalplates.config.background_intensity.low", 0.40f),
    NORMAL("vitalplates.config.background_intensity.normal", 0.65f),
    HIGH("vitalplates.config.background_intensity.high", 0.90f);

    private final String translationKey;
    private final float factor;

    BackgroundIntensity(String translationKey, float factor) {
        this.translationKey = translationKey;
        this.factor = factor;
    }

    public net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable(this.translationKey);
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public float getFactor() {
        return factor;
    }
}
