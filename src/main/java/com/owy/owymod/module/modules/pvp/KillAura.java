package com.owy.owymod.module.modules.pvp;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class KillAura extends Module {

    private final NumberSetting range = add(new NumberSetting("Reichweite", 3.6D, 1.0D, 6.0D, 0.1D, " Bloecke"));
    private final NumberSetting cps = add(new NumberSetting("Angriffe/Sekunde", 12.0D, 1.0D, 20.0D, 1.0D));
    private final BoolSetting targetPlayers = add(new BoolSetting("Spieler", true));
    private final BoolSetting targetMobs = add(new BoolSetting("Mobs", true));
    private final BoolSetting rotate = add(new BoolSetting("Ziel anvisieren", true));
    private final BoolSetting swing = add(new BoolSetting("Schlagen animieren", true));
    private final BoolSetting throughWalls = add(new BoolSetting("Durch Waende", true));

    private int cooldown;

    KillAura() {
        super("KillAura", Category.PVP, "Greift automatisch Ziele in Reichweite an.");
    }

    @Override
    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        if (mc.currentScreen != null) {
            if (cooldown > 0) cooldown--;
            return;
        }

        Entity target = findTarget(mc);
        if (target == null) {
            if (cooldown > 0) cooldown--;
            return;
        }

        if (rotate.get()) faceEntity(mc.player, target);

        if (cooldown <= 0) {
            mc.interactionManager.attackEntity(mc.player, target);
            if (swing.get()) mc.player.swingHand(Hand.MAIN_HAND);
            cooldown = Math.max(1, (int) Math.round(20.0D / Math.max(1.0D, cps.get())));
        } else {
            cooldown--;
        }
    }

    private Entity findTarget(MinecraftClient mc) {
        double r = range.get();
        Box searchBox = mc.player.getBoundingBox().expand(r);
        Entity best = null;
        double bestDist = Double.MAX_VALUE;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player || !entity.isAlive()) continue;

            boolean isPlayer = entity instanceof PlayerEntity;
            if (isPlayer) {
                if (!targetPlayers.get()) continue;
            } else {
                if (!(entity instanceof LivingEntity)) continue;
                if (!targetMobs.get()) continue;
            }

            if (!searchBox.intersects(entity.getBoundingBox())) continue;
            if (mc.player.distanceTo(entity) > r + 1.0D) continue;
            if (throughWalls.get() && !mc.player.canSee(entity)) continue;

            double dist = mc.player.squaredDistanceTo(entity);
            if (dist < bestDist) {
                bestDist = dist;
                best = entity;
            }
        }
        return best;
    }

    private void faceEntity(PlayerEntity player, Entity target) {
        Vec3d eyes = player.getEyePos();
        Vec3d targetCenter = target.getPos().add(0.0D, target.getHeight() * 0.5D, 0.0D);

        double dx = targetCenter.x - eyes.x;
        double dy = targetCenter.y - eyes.y;
        double dz = targetCenter.z - eyes.z;
        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0D);
        float pitch = (float) (-Math.toDegrees(Math.atan2(dy, horizontal)));

        player.setYaw(yaw);
        player.setPitch(pitch);
    }
}
