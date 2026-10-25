package com.owy.owymod.module;

public enum Category {
    PVP("PvP", 0xFFE05A5A),
    SURVIVAL("Survival", 0xFF5AC8E0),
    FUN("Fun & Movement", 0xFF8FE05A);

    private final String displayName;
    private final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public int getColor() { return color; }
}
