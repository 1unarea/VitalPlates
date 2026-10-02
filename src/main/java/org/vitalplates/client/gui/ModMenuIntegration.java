package org.vitalplates.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * ModMenu integration entrypoint for VitalPlates.
 * Connects the in-game mod list config button to {@link VitalPlatesConfigScreen}.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return VitalPlatesConfigScreen::new;
    }
}
