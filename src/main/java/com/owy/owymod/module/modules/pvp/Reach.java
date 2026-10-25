package com.owy.owymod.module.modules.pvp;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.NumberSetting;

public class Reach extends Module {

    private final NumberSetting blockReach = add(new NumberSetting("Block-Reichweite", 5.0D, 3.0D, 10.0D, 0.1D, " Bloecke"));
    private final NumberSetting entityReach = add(new NumberSetting("Entity-Reichweite", 4.5D, 3.0D, 10.0D, 0.1D, " Bloecke"));

    Reach() {
        super("Reach", Category.PVP, "Erhoeht die Reichweite zum Abbauen und zum Schlagen.");
    }

    public double getBlockReach() { return blockReach.get(); }
    public double getEntityReach() { return entityReach.get(); }
}
