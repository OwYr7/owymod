package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module {

    private final NumberSetting gamma = add(new NumberSetting("Gamma", 10.0D, 1.0D, 16.0D, 0.5D));

    private double previousGamma = 1.0D;

    Fullbright() {
        super("Fullbright", Category.SURVIVAL, "Maximale Helligkeit, auch in Hoehlen und nachts.");
    }

    @Override
    protected void onEnable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.options != null) {
            previousGamma = mc.options.getGamma().getValue();
        }
    }

    @Override
    protected void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null && mc.options != null) {
            mc.options.getGamma().setValue(previousGamma);
        }
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.options == null) return;
        if (mc.options.getGamma().getValue() != gamma.get()) {
            mc.options.getGamma().setValue(gamma.get());
        }
    }
}
