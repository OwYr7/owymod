package com.owy.owymod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.owy.owymod.OWYMod;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.ModuleManager;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import com.owy.owymod.module.setting.Setting;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Config() {}

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("owymod.json");
    }

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("version", OWYMod.MOD_VERSION);

            JsonObject modules = new JsonObject();
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                JsonObject moduleObject = new JsonObject();
                moduleObject.addProperty("enabled", module.isEnabled());

                JsonObject settings = new JsonObject();
                for (Setting<?> setting : module.getSettings()) {
                    if (setting instanceof BoolSetting boolSetting) {
                        settings.addProperty(setting.getName(), boolSetting.get());
                    } else if (setting instanceof NumberSetting numberSetting) {
                        settings.addProperty(setting.getName(), numberSetting.get());
                    }
                }
                moduleObject.add("settings", settings);
                modules.add(module.getName(), moduleObject);
            }
            root.add("modules", modules);

            Path path = file();
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(root));
        } catch (Exception e) {
            OWYMod.LOGGER.warn("[OWYMOD] Config konnte nicht gespeichert werden", e);
        }
    }

    public static void load() {
        try {
            Path path = file();
            if (!Files.exists(path)) return;

            JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            if (!root.has("modules")) return;

            JsonObject modules = root.getAsJsonObject("modules");
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                if (!modules.has(module.getName())) continue;
                JsonObject moduleObject = modules.getAsJsonObject(module.getName());

                if (moduleObject.has("settings")) {
                    JsonObject settings = moduleObject.getAsJsonObject("settings");
                    for (Setting<?> setting : module.getSettings()) {
                        if (!settings.has(setting.getName())) continue;
                        JsonElement element = settings.get(setting.getName());
                        if (setting instanceof BoolSetting boolSetting) {
                            boolSetting.set(element.getAsBoolean());
                        } else if (setting instanceof NumberSetting numberSetting) {
                            numberSetting.set(element.getAsDouble());
                        }
                    }
                }

                if (moduleObject.has("enabled") && moduleObject.get("enabled").getAsBoolean()) {
                    module.setEnabled(true);
                }
            }
        } catch (Exception e) {
            OWYMod.LOGGER.warn("[OWYMOD] Config konnte nicht geladen werden", e);
        }
    }
}
