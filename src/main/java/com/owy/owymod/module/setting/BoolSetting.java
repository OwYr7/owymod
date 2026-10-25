package com.owy.owymod.module.setting;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public boolean get() { return value; }
    public void set(boolean v) { this.value = v; }
    public void toggle() { this.value = !this.value; }

    @Override
    public String displayValue() { return value ? "AN" : "AUS"; }
}
