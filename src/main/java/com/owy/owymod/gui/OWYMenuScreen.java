package com.owy.owymod.gui;

import com.owy.owymod.OWYMod;
import com.owy.owymod.config.Config;
import com.owy.owymod.module.Category;
import com.owy.owymod.module.Module;
import com.owy.owymod.module.ModuleManager;
import com.owy.owymod.module.setting.BoolSetting;
import com.owy.owymod.module.setting.NumberSetting;
import com.owy.owymod.module.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.List;

public class OWYMenuScreen extends Screen {

    private static final int PANEL_W = 600;
    private static final int PANEL_H = 350;
    private static final int HEADER_H = 34;
    private static final int CAT_W = 120;
    private static final int SET_W = 215;
    private static final int ROW_H = 22;

    private static final int COL_ACCENT = 0xFF4FC3F7;
    private static final int COL_PANEL = 0xFF1B1B24;
    private static final int COL_PANEL_DARK = 0xFF131319;
    private static final int COL_TEXT = 0xFFE6E6E6;
    private static final int COL_DIM = 0xFF8A8A99;
    private static final int COL_ON = 0xFF57D977;
    private static final int COL_OFF = 0xFF4A4A56;
    private static final int COL_HOVER = 0x22FFFFFF;
    private static final int COL_SEL = 0x553FC1F7;

    private int panelX;
    private int panelY;
    private int catX;
    private int catY;
    private int listX;
    private int listY;
    private int listW;
    private int listH;
    private int setX;
    private int setY;
    private int setW;
    private int setH;

    private Category category = Category.PVP;
    private Module selectedModule;
    private double scroll;
    private NumberSetting dragging;

    public OWYMenuScreen() {
        super(Text.literal("OWYMOD"));
    }

    @Override
    protected void init() {
        panelX = (this.width - PANEL_W) / 2;
        panelY = (this.height - PANEL_H) / 2;

        catX = panelX + 8;
        catY = panelY + HEADER_H + 8;

        listX = panelX + CAT_W + 14;
        listY = panelY + HEADER_H + 8;
        listW = PANEL_W - CAT_W - SET_W - 34;
        listH = PANEL_H - HEADER_H - 18;

        setX = panelX + PANEL_W - SET_W - 10;
        setY = listY;
        setW = SET_W;
        setH = listH;

        super.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.fill(panelX - 1, panelY - 1, panelX + PANEL_W + 1, panelY + PANEL_H + 1, COL_ACCENT);
        context.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, COL_PANEL);
        context.fill(panelX, panelY, panelX + PANEL_W, panelY + HEADER_H, COL_PANEL_DARK);
        context.fill(panelX, panelY + HEADER_H - 1, panelX + PANEL_W, panelY + HEADER_H, COL_ACCENT);

        context.drawTextWithShadow(textRenderer, Text.literal("OWYMOD"), panelX + 12, panelY + 7, COL_ACCENT);
        context.drawTextWithShadow(textRenderer,
                Text.literal("v" + OWYMod.MOD_VERSION + "   aktiv: " + ModuleManager.INSTANCE.getEnabledCount()),
                panelX + 82, panelY + 8, COL_DIM);
        context.drawTextWithShadow(textRenderer, Text.literal("RShift / ESC schliesst"),
                panelX + PANEL_W - 12 - textRenderer.getWidth("RShift / ESC schliesst"), panelY + 8, COL_DIM);

        renderCategories(context, mouseX, mouseY);
        renderModuleList(context, mouseX, mouseY);
        renderSettings(context, mouseX, mouseY);
    }

    private void renderCategories(DrawContext context, int mouseX, int mouseY) {
        int y = catY;
        for (Category cat : Category.values()) {
            boolean selected = cat == category;
            boolean hovered = isHovered(catX, y, CAT_W - 8, 20, mouseX, mouseY);
            context.fill(catX, y, catX + CAT_W - 8, y + 20, selected ? COL_SEL : (hovered ? COL_HOVER : COL_PANEL_DARK));
            if (selected) context.fill(catX, y, catX + 2, y + 20, COL_ACCENT);
            context.drawTextWithShadow(textRenderer, Text.literal(cat.getDisplayName()), catX + 10, y + 6,
                    selected ? COL_ACCENT : COL_TEXT);
            y += 24;
        }
    }

    private void renderModuleList(DrawContext context, int mouseX, int mouseY) {
        context.fill(listX - 4, listY - 4, listX + listW + 4, listY + listH + 4, COL_PANEL_DARK);
        context.enableScissor(listX - 4, listY - 4, listX + listW + 4, listY + listH + 4);

        List<Module> modules = ModuleManager.INSTANCE.getModules(category);
        int y = listY - (int) scroll;

        for (Module module : modules) {
            boolean hovered = isHovered(listX, y, listW, ROW_H - 2, mouseX, mouseY);
            boolean selected = module == selectedModule;

            if (selected) context.fill(listX, y, listX + listW, y + ROW_H - 2, COL_SEL);
            else if (hovered) context.fill(listX, y, listX + listW, y + ROW_H - 2, COL_HOVER);

            context.drawTextWithShadow(textRenderer, Text.literal(module.getName()), listX + 8, y + 6,
                    module.isEnabled() ? COL_ON : COL_TEXT);

            int switchW = 26;
            int switchH = 12;
            int sx = listX + listW - switchW - 8;
            int sy = y + 4;
            context.fill(sx, sy, sx + switchW, sy + switchH, module.isEnabled() ? 0xFF1F7A3A : COL_OFF);
            int knobX = module.isEnabled() ? sx + switchW - switchH + 1 : sx + 1;
            context.fill(knobX, sy + 1, knobX + switchH - 2, sy + switchH - 1, 0xFFFFFFFF);

            y += ROW_H;
        }

        context.disableScissor();
    }

    private void renderSettings(DrawContext context, int mouseX, int mouseY) {
        context.fill(setX, setY - 4, setX + setW, setY + setH + 4, COL_PANEL_DARK);

        if (selectedModule == null) {
            context.drawTextWithShadow(textRenderer, Text.literal("Modul anklicken"), setX + 10, setY + 6, COL_DIM);
            return;
        }

        context.drawTextWithShadow(textRenderer, Text.literal(selectedModule.getName()), setX + 10, setY + 6, COL_ACCENT);

        int y = setY + 22;
        context.drawTextWrapped(textRenderer, Text.literal(selectedModule.getDescription()),
                setX + 10, y, setW - 20, COL_DIM);
        y += textRenderer.wrapLines(Text.literal(selectedModule.getDescription()), setW - 20).size() * 10 + 8;

        if (selectedModule.getSettings().isEmpty()) {
            context.drawTextWithShadow(textRenderer, Text.literal("Keine Einstellungen"), setX + 10, y, COL_DIM);
            return;
        }

        for (Setting<?> setting : selectedModule.getSettings()) {
            if (setting instanceof BoolSetting boolSetting) {
                context.drawTextWithShadow(textRenderer, Text.literal(boolSetting.getName()), setX + 10, y + 3, COL_TEXT);
                int switchW = 26;
                int switchH = 12;
                int sx = setX + setW - switchW - 10;
                context.fill(sx, y, sx + switchW, y + switchH, boolSetting.get() ? 0xFF1F7A3A : COL_OFF);
                int knobX = boolSetting.get() ? sx + switchW - switchH + 1 : sx + 1;
                context.fill(knobX, y + 1, knobX + switchH - 2, y + switchH - 1, 0xFFFFFFFF);
                y += 20;
            } else if (setting instanceof NumberSetting numberSetting) {
                context.drawTextWithShadow(textRenderer, Text.literal(numberSetting.getName()), setX + 10, y, COL_TEXT);
                String value = numberSetting.displayValue();
                context.drawTextWithShadow(textRenderer, Text.literal(value),
                        setX + setW - 10 - textRenderer.getWidth(value), y, COL_ACCENT);

                int barX = setX + 10;
                int barY = y + 12;
                int barW = setW - 20;
                int barH = 5;
                context.fill(barX, barY, barX + barW, barY + barH, 0xFF2A2A34);
                int filled = (int) (barW * numberSetting.fraction());
                context.fill(barX, barY, barX + filled, barY + barH, COL_ACCENT);
                context.fill(barX + filled - 1, barY - 2, barX + filled + 1, barY + barH + 2, 0xFFFFFFFF);
                y += 26;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        int catRowY = catY;
        for (Category cat : Category.values()) {
            if (isHovered(catX, catRowY, CAT_W - 8, 20, mouseX, mouseY)) {
                category = cat;
                selectedModule = null;
                scroll = 0.0D;
                return true;
            }
            catRowY += 24;
        }

        List<Module> modules = ModuleManager.INSTANCE.getModules(category);
        int rowY = listY - (int) scroll;
        for (Module module : modules) {
            if (isHovered(listX, rowY, listW, ROW_H - 2, mouseX, mouseY)) {
                int switchX = listX + listW - 26 - 8;
                if (mouseX >= switchX) {
                    module.toggle();
                    Config.save();
                } else {
                    selectedModule = module;
                }
                return true;
            }
            rowY += ROW_H;
        }

        if (selectedModule != null) {
            int y = setY + 22
                    + textRenderer.wrapLines(Text.literal(selectedModule.getDescription()), setW - 20).size() * 10 + 8;

            for (Setting<?> setting : selectedModule.getSettings()) {
                if (setting instanceof BoolSetting boolSetting) {
                    if (isHovered(setX + 10, y, setW - 20, 14, mouseX, mouseY)) {
                        boolSetting.toggle();
                        Config.save();
                        return true;
                    }
                    y += 20;
                } else if (setting instanceof NumberSetting numberSetting) {
                    if (isHovered(setX + 10, y + 8, setW - 20, 14, mouseX, mouseY)) {
                        dragging = numberSetting;
                        applySlider(numberSetting, mouseX);
                        return true;
                    }
                    y += 26;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging != null) {
            applySlider(dragging, mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging != null) {
            dragging = null;
            Config.save();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int contentHeight = ModuleManager.INSTANCE.getModules(category).size() * ROW_H;
        int maxScroll = Math.max(0, contentHeight - listH);
        scroll = Math.max(0.0D, Math.min(maxScroll, scroll - verticalAmount * 14.0D));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        int bound = InputUtil.fromTranslationKey(OWYMod.menuKey.getBoundKeyTranslationKey()).getCode();
        if (keyCode == bound) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        Config.save();
        super.close();
    }

    private void applySlider(NumberSetting setting, double mouseX) {
        int barX = setX + 10;
        int barW = setW - 20;
        double fraction = (mouseX - barX) / (double) barW;
        fraction = Math.max(0.0D, Math.min(1.0D, fraction));
        setting.set(setting.getMin() + fraction * (setting.getMax() - setting.getMin()));
    }

    private boolean isHovered(int x, int y, int w, int h, double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
