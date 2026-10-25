package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;

public class Fly extends Module {

    private final BoolSetting vanillaSpeed = add(new BoolSetting("Vanilla-Geschwindigkeit", true));
    private final NumberSetting speed = add(new NumberSetting("Geschwindigkeit", 1.0D, 0.5D, 5.0D, 0.1D, "x"));

    private boolean previousAllowFlying;
    private float previousFlySpeed = 0.05F;

    Fly() {
        super("Fly", Category.FUN, "Fliegen im Survival - rein clientseitig, der Server merkt nichts davon.");
    }

    @Override
    protected void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;
        PlayerAbilities abilities = mc.player.getAbilities();
        previousAllowFlying = abilities.allowFlying;
        previousFlySpeed = abilities.getFlySpeed();
        abilities.allowFlying = true;
        abilities.flying = true;
    }

    @Override
    protected void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;
        PlayerAbilities abilities = mc.player.getAbilities();
        boolean keepFlying = previousAllowFlying || mc.player.isCreative();
        abilities.allowFlying = keepFlying;
        if (!keepFlying) abilities.flying = false;
        abilities.setFlySpeed(previousFlySpeed);
        mc.player.fallDistance = 0.0F;
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.player == null) return;

        PlayerAbilities abilities = mc.player.getAbilities();
        if (!abilities.allowFlying) abilities.allowFlying = true;

        if (!abilities.flying && (!mc.player.isOnGround() || mc.options.jumpKey.isPressed())) {
            abilities.flying = true;
        }

        if (!vanillaSpeed.get()) {
            abilities.setFlySpeed((float) (speed.get() * 0.05D));
        } else {
            abilities.setFlySpeed(previousFlySpeed);
        }

        mc.player.fallDistance = 0.0F;
    }
}
