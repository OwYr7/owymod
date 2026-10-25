package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import net.minecraft.client.MinecraftClient;

public class Sprint extends Module {

    Sprint() {
        super("Sprint", Category.FUN, "Du rennst automatisch, ohne die Sprint-Taste zu halten.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (mc.player.isSneaking()) return;
        if (!mc.options.forwardKey.isPressed()) return;
        mc.player.setSprinting(true);
    }
}
