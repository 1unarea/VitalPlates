package org.vitalplates.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.vitalplates.config.BackgroundIntensity;
import org.vitalplates.config.TransitionMode;
import org.vitalplates.config.VitalPlatesConfig;

/**
 * In-game configuration GUI screen for VitalPlates, accessible from ModMenu.
 * Uses vanilla Minecraft Screen components with zero external GUI library dependencies.
 */
public class VitalPlatesConfigScreen extends Screen {

    private final Screen parent;
    private final VitalPlatesConfig config;

    public VitalPlatesConfigScreen(Screen parent) {
        super(Component.translatable("vitalplates.config.title"));
        this.parent = parent;
        this.config = VitalPlatesConfig.get();
    }

    @Override
    protected void init() {
        int widgetWidth = 280;
        int widgetHeight = 20;
        int startX = (this.width - widgetWidth) / 2;
        int currentY = 40;
        int spacing = 24;

        // 1. Mod Enabled
        this.addRenderableWidget(
            CycleButton.onOffBuilder(config.isEnabled())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.mod_status"),
                    (btn, value) -> config.setEnabled(value))
        );
        currentY += spacing;

        // 2. Transition Mode (Classic vs Spectrum)
        this.addRenderableWidget(
            CycleButton.<TransitionMode>builder(TransitionMode::getDisplayName, config.getTransitionMode())
                .withValues(TransitionMode.values())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.transition_mode"),
                    (btn, value) -> config.setTransitionMode(value))
        );
        currentY += spacing;

        // 3. Plate Background Tint (On / Off)
        this.addRenderableWidget(
            CycleButton.onOffBuilder(config.isTintBackground())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.tint_background"),
                    (btn, value) -> config.setTintBackground(value))
        );
        currentY += spacing;

        // 4. Background Intensity (Low / Normal / High)
        this.addRenderableWidget(
            CycleButton.<BackgroundIntensity>builder(BackgroundIntensity::getDisplayName, config.getBackgroundIntensity())
                .withValues(BackgroundIntensity.values())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.background_intensity"),
                    (btn, value) -> config.setBackgroundIntensity(value))
        );
        currentY += spacing;

        // 5. Affect Players (On / Off)
        this.addRenderableWidget(
            CycleButton.onOffBuilder(config.isAffectPlayers())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.affect_players"),
                    (btn, value) -> config.setAffectPlayers(value))
        );
        currentY += spacing;

        // 6. Affect Named Mobs (On / Off)
        this.addRenderableWidget(
            CycleButton.onOffBuilder(config.isAffectNamedMobs())
                .create(startX, currentY, widgetWidth, widgetHeight,
                    Component.translatable("vitalplates.config.affect_named_mobs"),
                    (btn, value) -> config.setAffectNamedMobs(value))
        );

        // Bottom Controls: Reset Defaults & Done
        int bottomButtonWidth = 135;
        int bottomY = this.height - 32;

        this.addRenderableWidget(
            Button.builder(Component.translatable("vitalplates.config.reset_defaults"), btn -> {
                config.resetDefaults();
                this.rebuildWidgets();
            }).bounds(this.width / 2 - bottomButtonWidth - 5, bottomY, bottomButtonWidth, widgetHeight).build()
        );

        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE, btn -> this.onClose())
                .bounds(this.width / 2 + 5, bottomY, bottomButtonWidth, widgetHeight).build()
        );
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(extractor, mouseX, mouseY, partialTick);
        extractor.centeredText(this.font, this.title, this.width / 2, 18, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        config.save();
        if (this.minecraft != null) {
            this.minecraft.setScreenAndShow(this.parent);
        }
    }
}
