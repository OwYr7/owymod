package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

    private final BoolSetting onlyWhenSneaking = add(new BoolSetting("Nur beim Schleichen", false));
    private final NumberSetting delay = add(new NumberSetting("Verzoegerung", 2.0D, 0.0D, 10.0D, 1.0D, " Ticks"));

    private int timer;

    Scaffold() {
        super("Scaffold", Category.SURVIVAL, "Platziert automatisch Bloecke unter deinen Fuessen (Air Blocks).");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;
        if (onlyWhenSneaking.get() && !mc.player.isSneaking()) return;

        if (timer > 0) {
            timer--;
            return;
        }

        BlockPos feet = mc.player.getBlockPos();
        BlockPos target = new BlockPos(feet.getX(), (int) Math.floor(mc.player.getY() - 1.0D), feet.getZ());

        if (!mc.world.getBlockState(target).isAir()) return;

        BlockPos support = target.down();
        if (mc.world.getBlockState(support).isAir()) return;

        int slot = findBlockSlot(mc);
        if (slot == -1) return;

        mc.player.getInventory().selectedSlot = slot;

        Vec3d hitVec = Vec3d.ofCenter(support);
        BlockHitResult hit = new BlockHitResult(hitVec, Direction.UP, support, false);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);

        timer = delay.getInt();
    }

    private int findBlockSlot(MinecraftClient mc) {
        PlayerInventory inventory = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem() instanceof BlockItem) return i;
        }
        return -1;
    }
}
