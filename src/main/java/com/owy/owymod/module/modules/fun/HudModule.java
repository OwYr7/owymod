package com.owy.owymod.module.modules.fun;

import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.setting.BoolSetting;

public class HudModule extends Module {

    private final BoolSetting watermark = add(new BoolSetting("OWYMOD-Schriftzug", true));
    private final BoolSetting moduleList = add(new BoolSetting("Aktive Module anzeigen", true));

    HudModule() {
        super("Watermark", Category.FUN, "HUD-Einblendung mit Schriftzug und Liste aktiver Module.");
    }

    public boolean showWatermark() { return watermark.get(); }
    public boolean showModuleList() { return moduleList.get(); }
}
