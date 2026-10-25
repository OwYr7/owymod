package com.owy.owymod.module.modules.pvp;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoClicker extends Module {

    private final NumberSetting cps = add(new NumberSetting("Klicks/Sekunde", 14.0D, 1.0D, 20.0D, 1.0D));

    private int cooldown;

    AutoClicker() {
        super("AutoClicker", Category.PVP, "Haelt die linke Maustaste automatisch mit hoher Klickrate.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null || mc.world == null) return;
        if (mc.currentScreen != null) return;
        if (!mc.options.attackKey.isPressed()) {
            cooldown = 0;
            return;
        }
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        HitResult hit = mc.crosshairTarget;
        if (hit instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity() != null && entityHit.getEntity().isAlive()) {
                mc.interactionManager.attackEntity(mc.player, entityHit.getEntity());
            }
        } else if (hit instanceof BlockHitResult blockHit && hit.getType() == HitResult.Type.BLOCK) {
            mc.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), blockHit.getSide());
        }
        mc.player.swingHand(Hand.MAIN_HAND);

        cooldown = Math.max(1, (int) Math.round(20.0D / Math.max(1.0D, cps.get())));
    }
}
