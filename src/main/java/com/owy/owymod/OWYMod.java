package com.owy.owymod;

import com.owy.owymod.config.Config;
import com.owy.owymod.gui.OWYMenuScreen;
import com.owy.owymod.hud.OWYHud;
import com.owy.owymod.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OWYMod implements ClientModInitializer {

    public static final String MOD_ID = "owymod";
    public static final String MOD_NAME = "OWYMOD";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[{}] Starte v{} fuer Minecraft 1.21.11 (Fabric)", MOD_NAME, MOD_VERSION);

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.owymod.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.owymod"
        ));

        ModuleManager.INSTANCE.init();
        Config.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                client.setScreen(new OWYMenuScreen());
            }
            ModuleManager.INSTANCE.handleKeybinds();
            ModuleManager.INSTANCE.tick();
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> ModuleManager.INSTANCE.renderWorld(context));
        HudRenderCallback.EVENT.register((context, tickCounter) -> OWYHud.render(context));

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());

        LOGGER.info("[{}] {} Module geladen.", MOD_NAME, ModuleManager.INSTANCE.getModules().size());
    }
}
