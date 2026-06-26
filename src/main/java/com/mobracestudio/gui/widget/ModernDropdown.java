package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernDropdown {
    private final int x, y, width, height;
    private final List<String> options = new ArrayList<>();
    private int selectedIndex = 0;
    private boolean expanded = false;
    private int maxVisibleOptions = 6;
    private Consumer<Integer> onSelect;
    private int hoveredIndex = -1;

    public ModernDropdown(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ModernDropdown addOption(String option) {
        options.add(option);
        return this;
    }

    public ModernDropdown addOptions(List<String> opts) {
        options.addAll(opts);
        return this;
    }

    public ModernDropdown setOnSelect(Consumer<Integer> callback) {
        this.onSelect = callback;
        return this;
    }

    public void setSelected(int index) {
        this.selectedIndex = Math.max(0, Math.min(index, options.size() - 1));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedValue() {
        return options.isEmpty() ? "" : options.get(selectedIndex);
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height);
        int bgColor = hovered ? GuiRenderHelper.COLOR_BG_LIGHT : GuiRenderHelper.COLOR_BG_MEDIUM;
        GuiRenderHelper.fill(ctx, x, y, width, height, bgColor);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        String displayText = options.isEmpty() ? "---" : options.get(selectedIndex);
        GuiRenderHelper.drawText(ctx, displayText, x + 8, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);

        GuiRenderHelper.drawText(ctx, "\u25BC", x + width - 16, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_SECONDARY);

        if (expanded) {
            int dropdownHeight = Math.min(options.size(), maxVisibleOptions) * height;
            GuiRenderHelper.fill(ctx, x, y + height, width, dropdownHeight, GuiRenderHelper.COLOR_BG_DARK);
            GuiRenderHelper.border(ctx, x, y + height, width, dropdownHeight, 1, GuiRenderHelper.COLOR_BORDER);

            for (int i = 0; i < Math.min(options.size(), maxVisibleOptions); i++) {
                int oy = y + height + i * height;
                boolean oh = GuiRenderHelper.isHovered(mouseX, mouseY, x, oy, width, height);
                if (oh) hoveredIndex = i;

                if (oh) {
                    GuiRenderHelper.fill(ctx, x + 1, oy, width - 2, height, GuiRenderHelper.COLOR_BG_LIGHT);
                }
                if (i == selectedIndex) {
                    GuiRenderHelper.fill(ctx, x + 1, oy, 2, height, GuiRenderHelper.COLOR_ACCENT);
                }
                GuiRenderHelper.drawText(ctx, options.get(i), x + 10, oy + height / 2 - 4, i == selectedIndex ? GuiRenderHelper.COLOR_ACCENT : GuiRenderHelper.COLOR_TEXT_PRIMARY);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height)) {
            expanded = !expanded;
            return true;
        }

        if (expanded) {
            int dropdownHeight = Math.min(options.size(), maxVisibleOptions) * height;
            for (int i = 0; i < Math.min(options.size(), maxVisibleOptions); i++) {
                int oy = y + height + i * height;
                if (GuiRenderHelper.isHovered(mouseX, mouseY, x, oy, width, height)) {
                    selectedIndex = i;
                    expanded = false;
                    if (onSelect != null) {
                        onSelect.accept(i);
                    }
                    return true;
                }
            }
            expanded = false;
        }
        return false;
    }
}
