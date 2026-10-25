package com.owy.owymod.module.modules.pvp;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

public class TriggerBot extends Module {

    private final NumberSetting range = add(new NumberSetting("Reichweite", 3.6D, 1.0D, 6.0D, 0.1D, " Bloecke"));
    private final NumberSetting delay = add(new NumberSetting("Verzoegerung", 1.0D, 0.0D, 10.0D, 1.0D, " Ticks"));
    private final BoolSetting fullCooldown = add(new BoolSetting("Volle Ladeanzeige abwarten", true));

    private int timer;

    TriggerBot() {
        super("TriggerBot", Category.PVP, "Schlaegt automatisch zu, sobald du ein Ziel anvisierst.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;

        if (timer > 0) {
            timer--;
            return;
        }

        if (!(mc.crosshairTarget instanceof EntityHitResult hit)) return;
        Entity target = hit.getEntity();
        if (target == null || !target.isAlive()) return;
        if (mc.player.distanceTo(target) > range.get() + 0.6D) return;
        if (fullCooldown.get() && mc.player.getAttackCooldownProgress(0.0F) < 1.0F) return;

        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        timer = delay.getInt();
    }
}
