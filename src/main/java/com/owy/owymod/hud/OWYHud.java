package com.owy.owymod.hud;

import com.owy.owymod.module.Module;
import com.owy.owymod.module.ModuleManager;
import com.owy.owymod.module.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class OWYHud {

    private OWYHud() {}

    public static void render(DrawContext context) {
        if (!Modules.HUD.isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;

        int x = 5;
        int y = 5;

        if (Modules.HUD.showWatermark()) {
            context.drawTextWithShadow(mc.textRenderer, Text.literal("OWYMOD"), x, y, 0xFF4FC3F7);
            context.drawTextWithShadow(mc.textRenderer, Text.literal("v1.0.0"),
                    x + mc.textRenderer.getWidth("OWYMOD") + 5, y, 0xFF6B6B78);
            y += 12;
        }

        if (Modules.HUD.showModuleList()) {
            List<Module> active = new ArrayList<>();
            for (Module module : ModuleManager.INSTANCE.getModules()) {
                if (module.isEnabled() && module != Modules.HUD) active.add(module);
            }
            active.sort(Comparator.comparingInt((Module m) -> -mc.textRenderer.getWidth(m.getName())));

            for (Module module : active) {
                context.drawTextWithShadow(mc.textRenderer, Text.literal(module.getName()), x, y,
                        module.getCategory().getColor());
                y += 10;
            }
        }
    }
}
