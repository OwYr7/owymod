package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    private final NumberSetting minFallDistance = add(new NumberSetting("Ab Fallhoehe", 2.0D, 1.0D, 20.0D, 0.5D, " Bloecke"));

    NoFall() {
        super("NoFall", Category.SURVIVAL, "Verhindert Fallschaden, indem dem Server Bodenkontakt gemeldet wird.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        if (mc.player.fallDistance <= minFallDistance.get()) return;

        mc.getNetworkHandler().sendPacket(
                new PlayerMoveC2SPacket.OnGroundOnly(true, mc.player.horizontalCollision));
    }
}
