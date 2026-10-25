package com.owy.owymod.mixin;

import com.owy.owymod.module.Modules;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true, require = 0)
    private void owy$noHurtCam(CallbackInfo ci) {
        if (Modules.NO_HURT_CAM.isEnabled()) ci.cancel();
    }
}
