package com.owy.owymod.mixin;

import com.owy.owymod.module.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    /** Criticals: kurz vor dem Angriff "nicht am Boden" melden -> Server rechnet einen Krit. */
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void owy$criticals(CallbackInfo ci) {
        if (!Modules.CRITICALS.isEnabled()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        PlayerEntity player = mc.player;
        if (player.isOnGround() && !player.isSprinting() && !player.isTouchingWater()) {
            mc.getNetworkHandler().sendPacket(
                    new PlayerMoveC2SPacket.OnGroundOnly(false, player.horizontalCollision));
        }
    }

    @Inject(method = "getReachDistance", at = @At("HEAD"), cancellable = true, require = 0)
    private void owy$reach(CallbackInfoReturnable<Double> cir) {
        if (Modules.REACH.isEnabled()) {
            cir.setReturnValue(Modules.REACH.getBlockReach());
        }
    }
}
