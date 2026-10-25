package com.owy.owymod.module;

import com.owy.owymod.OWYMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    public static final ModuleManager INSTANCE = new ModuleManager();

    private final List<Module> modules = new ArrayList<>();
    private final Map<Module, KeyBinding> keybinds = new HashMap<>();

    private ModuleManager() {}

    public void init() {
        for (Module module : Modules.ALL) register(module);
    }

    private void register(Module module) {
        modules.add(module);
        KeyBinding binding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                module.getKeyTranslationKey(),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.owymod"
        ));
        keybinds.put(module, binding);
    }

    public List<Module> getModules() { return modules; }

    public List<Module> getModules(Category category) {
        List<Module> out = new ArrayList<>();
        for (Module m : modules) if (m.getCategory() == category) out.add(m);
        return out;
    }

    public int getEnabledCount() {
        int n = 0;
        for (Module m : modules) if (m.isEnabled()) n++;
        return n;
    }

    public void handleKeybinds() {
        for (Map.Entry<Module, KeyBinding> entry : keybinds.entrySet()) {
            while (entry.getValue().wasPressed()) {
                entry.getKey().toggle();
            }
        }
    }

    public void tick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            try {
                module.onTick();
            } catch (Throwable t) {
                OWYMod.LOGGER.error("[OWYMOD] Tick-Fehler in " + module.getName(), t);
            }
        }
    }

    public void renderWorld(WorldRenderContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        for (Module module : modules) {
            if (!module.isEnabled()) continue;
            try {
                module.onRenderWorld(context);
            } catch (Throwable t) {
                OWYMod.LOGGER.error("[OWYMOD] Render-Fehler in " + module.getName(), t);
            }
        }
    }
}
