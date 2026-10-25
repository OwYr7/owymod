package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Module {

    AutoTool() {
        super("AutoTool", Category.SURVIVAL, "Waehlt automatisch das schnellste Werkzeug fuer den anvisierten Block.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;
        if (!mc.options.attackKey.isPressed()) return;
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) return;

        BlockPos pos = hit.getBlockPos();
        if (mc.world.getBlockState(pos).isAir()) return;

        PlayerInventory inventory = mc.player.getInventory();
        int bestSlot = -1;
        float bestSpeed = 1.0F;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;
            float speed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos));
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        if (bestSlot != -1 && inventory.selectedSlot != bestSlot) {
            inventory.selectedSlot = bestSlot;
        }
    }
}
