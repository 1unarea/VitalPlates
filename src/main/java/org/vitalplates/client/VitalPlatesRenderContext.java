package org.vitalplates.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * Thread-local render context for transferring active entity render state into
 * submitNameTag calls during nameplate rendering.
 */
public final class VitalPlatesRenderContext {

    public static final ThreadLocal<EntityRenderState> CURRENT_STATE = new ThreadLocal<>();

    private VitalPlatesRenderContext() {
        // Utility class
    }
}
