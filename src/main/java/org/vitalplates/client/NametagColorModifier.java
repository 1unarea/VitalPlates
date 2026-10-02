package org.vitalplates.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.vitalplates.config.VitalPlatesConfig;
import org.vitalplates.util.ColorGradientCalculator;

/**
 * Pure client-side nametag modifier for VitalPlates.
 * <p>
 * Identifies eligible entities (players and custom-named living entities),
 * calculates client-side health percentage, applies dynamic text color styles
 * and sets up background plate tinting based on active user configuration.
 */
public final class NametagColorModifier {

    private NametagColorModifier() {
        // Utility class
    }

    /**
     * Determines whether an entity is eligible for dynamic health nametag coloring
     * according to the active configuration.
     *
     * @param entity the entity being evaluated
     * @return true if enabled and matches configured targets (player / named mob)
     */
    public static boolean isTargetEntity(Entity entity) {
        VitalPlatesConfig config = VitalPlatesConfig.get();
        if (!config.isEnabled()) {
            return false;
        }
        if (entity instanceof Player) {
            return config.isAffectPlayers();
        }
        if (entity instanceof LivingEntity living) {
            return config.isAffectNamedMobs() && living.hasCustomName();
        }
        return false;
    }

    /**
     * Calculates the health ratio for a living entity.
     */
    public static float getHealthRatio(LivingEntity living) {
        return ColorGradientCalculator.getHealthRatio(
                living.getHealth(),
                living.getMaxHealth(),
                living.getAbsorptionAmount()
        );
    }

    /**
     * Modifies a nametag component with the dynamic health color of the target living entity.
     *
     * @param original the original nametag component
     * @param living   the living entity whose health to measure
     * @return a new Component styled with the dynamic health color, or original if disabled
     */
    public static Component applyHealthColor(Component original, LivingEntity living) {
        if (original == null) {
            return null;
        }

        VitalPlatesConfig config = VitalPlatesConfig.get();
        if (!config.isEnabled()) {
            return original;
        }

        float ratio = getHealthRatio(living);
        int rgb = ColorGradientCalculator.getTextColorRgb(ratio, config.getTransitionMode());
        return applyColor(original, rgb);
    }

    /**
     * Applies an RGB color to a Component while preserving its contents and styles
     * across all sibling components.
     *
     * @param component the source component
     * @param rgb       the 24-bit RGB integer (0xRRGGBB)
     * @return a newly constructed MutableComponent styled with the RGB color
     */
    public static MutableComponent applyColor(Component component, int rgb) {
        TextColor textColor = TextColor.fromRgb(rgb);
        return applyColorRecursively(component, textColor);
    }

    private static MutableComponent applyColorRecursively(Component component, TextColor textColor) {
        Style updatedStyle = component.getStyle().withColor(textColor);
        MutableComponent copy = MutableComponent.create(component.getContents()).setStyle(updatedStyle);
        for (Component sibling : component.getSiblings()) {
            copy.append(applyColorRecursively(sibling, textColor));
        }
        return copy;
    }
}
