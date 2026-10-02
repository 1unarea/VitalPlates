package org.vitalplates.mixin;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.vitalplates.client.VitalPlatesRenderStateExtension;
import org.vitalplates.config.VitalPlatesConfig;
import org.vitalplates.util.ColorGradientCalculator;

/**
 * Mixin into EntityRenderState to carry health ratio and compute dynamic background
 * plate color during nametag rendering.
 */
@Mixin(EntityRenderState.class)
public abstract class EntityRenderStateMixin implements VitalPlatesRenderStateExtension {

    @Unique
    private float vitalplates$healthRatio = 1.0f;
    @Unique
    private boolean vitalplates$hasCustomBackground = false;

    @Override
    public int vitalplates$getCustomBackgroundColor(float baseOpacity) {
        VitalPlatesConfig config = VitalPlatesConfig.get();
        return ColorGradientCalculator.getPlateBackgroundColor(
                baseOpacity,
                this.vitalplates$healthRatio,
                config.isTintBackground(),
                config.getBackgroundIntensity()
        );
    }

    @Override
    public void vitalplates$setCustomBackground(float healthRatio) {
        this.vitalplates$healthRatio = healthRatio;
        this.vitalplates$hasCustomBackground = true;
    }

    @Override
    public boolean vitalplates$hasCustomBackground() {
        return this.vitalplates$hasCustomBackground;
    }
}
