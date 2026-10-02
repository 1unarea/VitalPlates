package org.vitalplates.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration model and manager for VitalPlates.
 * Saves and loads options to config/vitalplates.json using Gson.
 */
public class VitalPlatesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("vitalplates/config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("vitalplates.json");

    private static VitalPlatesConfig INSTANCE;

    // Configurable options
    private boolean enabled = true;
    private TransitionMode transitionMode = TransitionMode.CLASSIC;
    private boolean tintBackground = true;
    private BackgroundIntensity backgroundIntensity = BackgroundIntensity.NORMAL;
    private boolean affectPlayers = true;
    private boolean affectNamedMobs = true;

    public static VitalPlatesConfig get() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    public static VitalPlatesConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                VitalPlatesConfig loaded = GSON.fromJson(reader, VitalPlatesConfig.class);
                if (loaded != null) {
                    LOGGER.info("VitalPlates configuration loaded successfully.");
                    return loaded;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load VitalPlates config, using default settings.", e);
            }
        }
        VitalPlatesConfig config = new VitalPlatesConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
                LOGGER.info("VitalPlates configuration saved.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save VitalPlates config.", e);
        }
    }

    public void resetDefaults() {
        this.enabled = true;
        this.transitionMode = TransitionMode.CLASSIC;
        this.tintBackground = true;
        this.backgroundIntensity = BackgroundIntensity.NORMAL;
        this.affectPlayers = true;
        this.affectNamedMobs = true;
    }

    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TransitionMode getTransitionMode() {
        return transitionMode != null ? transitionMode : TransitionMode.CLASSIC;
    }

    public void setTransitionMode(TransitionMode transitionMode) {
        this.transitionMode = transitionMode;
    }

    public boolean isTintBackground() {
        return tintBackground;
    }

    public void setTintBackground(boolean tintBackground) {
        this.tintBackground = tintBackground;
    }

    public BackgroundIntensity getBackgroundIntensity() {
        return backgroundIntensity != null ? backgroundIntensity : BackgroundIntensity.NORMAL;
    }

    public void setBackgroundIntensity(BackgroundIntensity backgroundIntensity) {
        this.backgroundIntensity = backgroundIntensity;
    }

    public boolean isAffectPlayers() {
        return affectPlayers;
    }

    public void setAffectPlayers(boolean affectPlayers) {
        this.affectPlayers = affectPlayers;
    }

    public boolean isAffectNamedMobs() {
        return affectNamedMobs;
    }

    public void setAffectNamedMobs(boolean affectNamedMobs) {
        this.affectNamedMobs = affectNamedMobs;
    }
}
