package com.owy.owymod.module.setting;

import java.util.Locale;

public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;
    private final String suffix;

    public NumberSetting(String name, double defaultValue, double min, double max, double step) {
        this(name, defaultValue, min, max, step, "");
    }

    public NumberSetting(String name, double defaultValue, double min, double max, double step, String suffix) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
        this.suffix = suffix;
    }

    public double get() { return value; }
    public int getInt() { return (int) Math.round(value); }
    public float getFloat() { return value.floatValue(); }
    public double getMin() { return min; }
    public double getMax() { return max; }

    public void set(double v) {
        double clamped = Math.max(min, Math.min(max, v));
        if (step > 0.0D) clamped = Math.round(clamped / step) * step;
        this.value = Math.max(min, Math.min(max, clamped));
    }

    public double fraction() {
        double span = max - min;
        return span <= 0.0D ? 0.0D : (value - min) / span;
    }

    @Override
    public String displayValue() {
        if (step >= 1.0D) return getInt() + suffix;
        return String.format(Locale.ROOT, "%.2f", value) + suffix;
    }
}
