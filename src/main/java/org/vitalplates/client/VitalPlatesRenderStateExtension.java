package org.vitalplates.client;

/**
 * Duck interface mixed into EntityRenderState to carry custom nametag background
 * color data from state extraction to the submission phase.
 */
public interface VitalPlatesRenderStateExtension {

    int vitalplates$getCustomBackgroundColor(float baseOpacity);

    void vitalplates$setCustomBackground(float healthRatio);

    boolean vitalplates$hasCustomBackground();
}
