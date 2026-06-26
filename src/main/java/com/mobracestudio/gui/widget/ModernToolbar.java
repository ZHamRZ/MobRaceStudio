package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernToolbar {
    private final int x, y, width, height;
    private final List<ToolbarButton> buttons = new ArrayList<>();
    private Consumer<Integer> onToolSelect;
    private int selectedTool = -1;

    public ModernToolbar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ModernToolbar addTool(String icon, String tooltip) {
        buttons.add(new ToolbarButton(icon, tooltip));
        return this;
    }

    public ModernToolbar setOnToolSelect(Consumer<Integer> callback) {
        this.onToolSelect = callback;
        return this;
    }

    public void selectTool(int index) {
        this.selectedTool = index;
    }

    public int getSelectedTool() {
        return selectedTool;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.fill(ctx, x, y + height - 1, width, 1, GuiRenderHelper.COLOR_BORDER);

        int btnSize = height - 8;
        int gap = 4;
        int startX = x + 6;

        for (int i = 0; i < buttons.size(); i++) {
            int bx = startX + i * (btnSize + gap);
            boolean selected = i == selectedTool;
            boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, bx, y + 4, btnSize, btnSize);

            int bg = selected ? GuiRenderHelper.COLOR_ACCENT : (hovered ? GuiRenderHelper.COLOR_BG_LIGHT : 0);
            if (bg != 0) {
                GuiRenderHelper.fill(ctx, bx, y + 4, btnSize, btnSize, bg);
            }
            if (selected) {
                GuiRenderHelper.border(ctx, bx, y + 4, btnSize, btnSize, 1, GuiRenderHelper.COLOR_ACCENT_HOVER);
            }

            GuiRenderHelper.drawTextCentered(ctx, buttons.get(i).icon, bx + btnSize / 2, y + 4 + btnSize / 2, GuiRenderHelper.COLOR_TEXT_PRIMARY);

            if (hovered && buttons.get(i).tooltip != null) {
                int tw = GuiRenderHelper.textWidth(buttons.get(i).tooltip);
                int tx = Math.min(bx, ctx.getScaledWindowWidth() - tw - 10);
                int ty = y - 22;
                GuiRenderHelper.fill(ctx, tx - 4, ty - 2, tw + 8, 14, GuiRenderHelper.COLOR_BG_DARK);
                GuiRenderHelper.border(ctx, tx - 4, ty - 2, tw + 8, 14, 1, GuiRenderHelper.COLOR_BORDER);
                GuiRenderHelper.drawText(ctx, buttons.get(i).tooltip, tx, ty, GuiRenderHelper.COLOR_TEXT_PRIMARY);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int btnSize = height - 8;
        int gap = 4;
        int startX = x + 6;

        for (int i = 0; i < buttons.size(); i++) {
            int bx = startX + i * (btnSize + gap);
            if (GuiRenderHelper.isHovered(mouseX, mouseY, bx, y + 4, btnSize, btnSize)) {
                selectedTool = i;
                if (onToolSelect != null) {
                    onToolSelect.accept(i);
                }
                return true;
            }
        }
        return false;
    }

    public static class ToolbarButton {
        public final String icon;
        public final String tooltip;

        public ToolbarButton(String icon, String tooltip) {
            this.icon = icon;
            this.tooltip = tooltip;
        }
    }
}
