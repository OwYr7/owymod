package com.owy.owymod.mixin;

import com.owy.owymod.module.Modules;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true, require = 0)
    private void owy$blockReach(CallbackInfoReturnable<Double> cir) {
        if (Modules.REACH.isEnabled()) {
            cir.setReturnValue(Modules.REACH.getBlockReach());
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true, require = 0)
    private void owy$entityReach(CallbackInfoReturnable<Double> cir) {
        if (Modules.REACH.isEnabled()) {
            cir.setReturnValue(Modules.REACH.getEntityReach());
        }
    }
}
