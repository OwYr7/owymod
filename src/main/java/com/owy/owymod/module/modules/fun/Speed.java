package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    private final NumberSetting multiplier = add(new NumberSetting("Multiplikator", 1.25D, 1.05D, 2.0D, 0.05D, "x"));

    Speed() {
        super("Speed", Category.FUN, "Erhoeht das Lauftempo zu Fuss.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!mc.player.isOnGround()) return;
        if (mc.player.isSneaking() || mc.player.isTouchingWater()) return;
        if (!(mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed()
                || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed())) return;

        Vec3d velocity = mc.player.getVelocity();
        double factor = multiplier.get();
        mc.player.setVelocity(velocity.x * factor, velocity.y, velocity.z * factor);
    }
}
