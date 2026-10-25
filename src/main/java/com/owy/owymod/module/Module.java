package com.owy.owymod.module;

import com.owy.owymod.OWYMod;
import com.owy.owymod.module.setting.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();

    private boolean enabled;

    protected Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public List<Setting<?>> getSettings() { return settings; }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (this.enabled == value) return;
        this.enabled = value;
        try {
            if (value) onEnable();
            else onDisable();
        } catch (Throwable t) {
            OWYMod.LOGGER.error("[OWYMOD] Fehler beim Umschalten von " + name, t);
        }
    }

    protected void onEnable() {}
    protected void onDisable() {}

    /** Wird jeden Client-Tick aufgerufen, solange das Modul aktiv ist. */
    public void onTick() {}

    /** Wird jeden Frame in der Welt aufgerufen, solange das Modul aktiv ist. */
    public void onRenderWorld(WorldRenderContext context) {}

    protected <T extends Setting<?>> T add(T setting) {
        this.settings.add(setting);
        return setting;
    }

    public String getKeyTranslationKey() {
        return "key.owymod." + name.toLowerCase(Locale.ROOT);
    }
}
