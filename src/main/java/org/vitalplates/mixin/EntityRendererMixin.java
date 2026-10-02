package org.vitalplates.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vitalplates.client.NametagColorModifier;
import org.vitalplates.client.VitalPlatesRenderContext;
import org.vitalplates.client.VitalPlatesRenderStateExtension;

/**
 * Fabric Mixin for {@link EntityRenderer}.
 * <p>
 * Intercepts nametag Component creation to recolor the text, attaches health data
 * to the entity render state, and binds the active state during nameplate submission.
 */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @Inject(method = "getNameTag", at = @At("RETURN"), cancellable = true)
    private void vitalplates$modifyNameTag(Entity entity, CallbackInfoReturnable<Component> cir) {
        Component original = cir.getReturnValue();
        if (original == null) {
            return;
        }

        if (NametagColorModifier.isTargetEntity(entity)) {
            Component colored = NametagColorModifier.applyHealthColor(original, (LivingEntity) entity);
            if (colored != null) {
                cir.setReturnValue(colored);
            }
        }
    }

    @Inject(
        method = "extractNameTags(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;FDD)V",
        at = @At("RETURN")
    )
    private void vitalplates$afterExtractNameTags(Entity entity, EntityRenderState state, float tickDelta, double d, double e, CallbackInfo ci) {
        if (state.nameTag != null && entity instanceof LivingEntity living && NametagColorModifier.isTargetEntity(living)) {
            float ratio = NametagColorModifier.getHealthRatio(living);
            ((VitalPlatesRenderStateExtension) state).vitalplates$setCustomBackground(ratio);
        }
    }

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V",
        at = @At("HEAD")
    )
    private void vitalplates$beforeSubmitNameDisplay(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, int offset, CallbackInfo ci) {
        VitalPlatesRenderContext.CURRENT_STATE.set(state);
    }

    @Inject(
        method = "submitNameDisplay(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;I)V",
        at = @At("RETURN")
    )
    private void vitalplates$afterSubmitNameDisplay(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, int offset, CallbackInfo ci) {
        VitalPlatesRenderContext.CURRENT_STATE.remove();
    }
}
