package com.owy.owymod.module;

import com.owy.owymod.module.modules.fun.*;
import com.owy.owymod.module.modules.pvp.*;
import com.owy.owymod.module.modules.survival.*;

import java.util.List;

public final class Modules {

    // --- PvP ---
    public static final KillAura KILL_AURA = new KillAura();
    public static final TriggerBot TRIGGER_BOT = new TriggerBot();
    public static final Reach REACH = new Reach();
    public static final AutoClicker AUTO_CLICKER = new AutoClicker();
    public static final Criticals CRITICALS = new Criticals();

    // --- Survival ---
    public static final AutoTotem AUTO_TOTEM = new AutoTotem();
    public static final Esp ESP = new Esp();
    public static final Fullbright FULLBRIGHT = new Fullbright();
    public static final AutoTool AUTO_TOOL = new AutoTool();
    public static final Scaffold SCAFFOLD = new Scaffold();
    public static final NoFall NO_FALL = new NoFall();

    // --- Fun & Movement ---
    public static final Fly FLY = new Fly();
    public static final Jesus JESUS = new Jesus();
    public static final Speed SPEED = new Speed();
    public static final Sprint SPRINT = new Sprint();
    public static final Step STEP = new Step();
    public static final NoHurtCam NO_HURT_CAM = new NoHurtCam();
    public static final HudModule HUD = new HudModule();

    public static final List<Module> ALL = List.of(
            KILL_AURA, TRIGGER_BOT, REACH, AUTO_CLICKER, CRITICALS,
            AUTO_TOTEM, ESP, FULLBRIGHT, AUTO_TOOL, SCAFFOLD, NO_FALL,
            FLY, JESUS, SPEED, SPRINT, STEP, NO_HURT_CAM, HUD
    );

    private Modules() {}
}
