package org.vitalplates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vitalplates.client.VitalPlatesRenderContext;
import org.vitalplates.client.VitalPlatesRenderStateExtension;

/**
 * Mixin into SubmitNodeCollection to dynamically tint the translucent background
 * plate of entity nametags ("arkaplanın rengi de hafif kızarsın").
 */
@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin {

    @Redirect(
        method = "submitNameTag",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ARGB;color(FI)I",
            ordinal = 0
        )
    )
    private int vitalplates$redirectBackgroundColor(float alpha, int baseColor,
                                                    PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component nameTag, boolean discrete, int lightCoords, CameraRenderState cameraRenderState) {
        EntityRenderState state = VitalPlatesRenderContext.CURRENT_STATE.get();
        if (state instanceof VitalPlatesRenderStateExtension ext && ext.vitalplates$hasCustomBackground()) {
            if (state.nameTag == nameTag) {
                return ext.vitalplates$getCustomBackgroundColor(alpha);
            }
        }
        return ARGB.color(alpha, baseColor);
    }
}
