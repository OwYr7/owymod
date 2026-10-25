package com.owy.owymod.module.modules.survival;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class Esp extends Module {

    private final NumberSetting range = add(new NumberSetting("Reichweite", 64.0D, 8.0D, 256.0D, 8.0D, " Bloecke"));
    private final BoolSetting players = add(new BoolSetting("Spieler", true));
    private final BoolSetting mobs = add(new BoolSetting("Mobs", true));
    private final BoolSetting tracers = add(new BoolSetting("Linien zum Ziel", false));
    private final BoolSetting healthColor = add(new BoolSetting("Farbe nach Gesundheit", true));

    Esp() {
        super("ESP", Category.SURVIVAL, "Wallhack: zeichnet Kaesten um Spieler und Mobs durch Waende.");
    }

    @Override
    public void onRenderWorld(WorldRenderContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        MatrixStack matrices = context.matrixStack();
        if (matrices == null) return;

        Vec3d camera = context.camera().getPos();
        VertexConsumerProvider.Immediate consumers = mc.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer lines = consumers.getBuffer(RenderLayer.getLines());

        double maxRange = range.get();
        boolean drawTracers = tracers.get();
        boolean useHealth = healthColor.get();

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || !entity.isAlive()) continue;

            boolean isPlayer = entity instanceof PlayerEntity;
            if (isPlayer) {
                if (!players.get()) continue;
            } else {
                if (!(entity instanceof LivingEntity)) continue;
                if (!mobs.get()) continue;
            }

            if (mc.player.distanceTo(entity) > maxRange) continue;

            float r;
            float g;
            float b;

            if (useHealth && entity instanceof LivingEntity living) {
                float ratio = Math.max(0.0F, Math.min(1.0F, living.getHealth() / Math.max(1.0F, living.getMaxHealth())));
                r = 1.0F - ratio;
                g = ratio;
                b = 0.15F;
            } else {
                r = isPlayer ? 1.0F : 0.35F;
                g = isPlayer ? 0.35F : 1.0F;
                b = 0.35F;
            }

            Box box = entity.getBoundingBox().offset(-camera.x, -camera.y, -camera.z);
            VertexRendering.drawBox(matrices, lines, box, r, g, b, 1.0F);

            if (drawTracers) {
                Vec3d eye = mc.player.getEyePos();
                float ox = (float) (eye.x - camera.x);
                float oy = (float) (eye.y - camera.y);
                float oz = (float) (eye.z - camera.z);
                float tx = (float) (entity.getX() - camera.x);
                float ty = (float) (entity.getY() + entity.getHeight() * 0.5D - camera.y);
                float tz = (float) (entity.getZ() - camera.z);
                line(matrices, lines, ox, oy, oz, tx, ty, tz, r, g, b);
            }
        }

        consumers.draw(RenderLayer.getLines());
    }

    private void line(MatrixStack matrices, VertexConsumer consumer,
                      float x1, float y1, float z1, float x2, float y2, float z2,
                      float r, float g, float b) {
        MatrixStack.Entry entry = matrices.peek();
        consumer.vertex(entry, x1, y1, z1).color(r, g, b, 1.0F).normal(entry, 0.0F, 1.0F, 0.0F);
        consumer.vertex(entry, x2, y2, z2).color(r, g, b, 1.0F).normal(entry, 0.0F, 1.0F, 0.0F);
    }
}
