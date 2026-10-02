package org.vitalplates;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client entrypoint for VitalPlates.
 * Initializes the client-side health nametag gradient system.
 */
public class VitalPlatesClient implements ClientModInitializer {

    public static final String MOD_ID = "vitalplates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("VitalPlates initialized: dynamic health nametag gradient system active.");
    }
}
