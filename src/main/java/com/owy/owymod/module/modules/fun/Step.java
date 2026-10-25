package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public class Step extends Module {

    private final NumberSetting height = add(new NumberSetting("Stufshoehe", 1.5D, 0.6D, 3.0D, 0.1D, " Bloecke"));

    Step() {
        super("Step", Category.FUN, "Du steigst automatisch hoehere Bloecke hoch, ohne zu springen.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        mc.player.setStepHeight(height.getFloat());
    }

    @Override
    protected void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) mc.player.setStepHeight(0.6F);
    }
}
