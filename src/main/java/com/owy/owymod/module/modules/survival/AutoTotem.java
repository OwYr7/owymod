package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {

    private final NumberSetting delay = add(new NumberSetting("Verzoegerung", 2.0D, 0.0D, 20.0D, 1.0D, " Ticks"));

    private int timer;

    AutoTotem() {
        super("AutoTotem", Category.SURVIVAL, "Zieht automatisch ein Totem der Unsterblichkeit in die Offhand.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;

        PlayerEntity player = mc.player;
        if (player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
            timer = 0;
            return;
        }
        if (timer > 0) {
            timer--;
            return;
        }

        PlayerInventory inventory = player.getInventory();
        int found = -1;
        for (int i = 0; i < 36; i++) {
            if (inventory.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                found = i;
                break;
            }
        }
        if (found == -1) return;

        int syncId = player.playerScreenHandler.syncId;

        if (found < 9) {
            mc.interactionManager.clickSlot(syncId, 36 + found, 40, SlotActionType.SWAP, player);
        } else {
            mc.interactionManager.clickSlot(syncId, found, 0, SlotActionType.PICKUP, player);
            mc.interactionManager.clickSlot(syncId, 45, 0, SlotActionType.PICKUP, player);
            mc.interactionManager.clickSlot(syncId, found, 0, SlotActionType.PICKUP, player);
        }

        timer = delay.getInt();
    }
}
