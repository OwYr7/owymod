package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Jesus extends Module {

    Jesus() {
        super("Jesus", Category.FUN, "Laeuft ueber Wasser. Zum Abtauchen einfach schleichen.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isSneaking()) return;
        if (!mc.player.isTouchingWater() && !mc.player.isSubmergedInWater()) return;

        BlockPos below = mc.player.getBlockPos().down();
        if (!mc.world.getFluidState(below).isIn(FluidTags.WATER)) return;

        Vec3d velocity = mc.player.getVelocity();
        double surfaceY = below.getY() + 1.0D;

        if (mc.player.getY() < surfaceY - 0.05D) {
            mc.player.setVelocity(velocity.x, 0.14D, velocity.z);
        } else {
            double vy = mc.options.jumpKey.isPressed() ? 0.42D : 0.0D;
            mc.player.setVelocity(velocity.x, vy, velocity.z);
            mc.player.setOnGround(true);
        }
        mc.player.fallDistance = 0.0F;
    }
}
