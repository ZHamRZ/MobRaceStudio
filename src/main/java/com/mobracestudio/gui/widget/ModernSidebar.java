package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernSidebar {
    private final int x, y, width, height;
    private final int itemHeight = 40;
    private final List<SidebarItem> items = new ArrayList<>();
    private int selectedIndex = -1;
    private Consumer<Integer> onSelect;
    private int scrollOffset = 0;
    private String title;

    public ModernSidebar(int x, int y, int width, int height, String title) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public ModernSidebar addItem(String icon, String label, String description) {
        items.add(new SidebarItem(icon, label, description));
        return this;
    }

    public ModernSidebar setOnSelect(Consumer<Integer> onSelect) {
        this.onSelect = onSelect;
        return this;
    }

    public void select(int index) {
        this.selectedIndex = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public SidebarItem getSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < items.size()) {
            return items.get(selectedIndex);
        }
        return null;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_DARK);

        GuiRenderHelper.fill(ctx, x + width - 1, y, 1, height, GuiRenderHelper.COLOR_BORDER);

        if (title != null) {
            GuiRenderHelper.drawText(ctx, title, x + 12, y + 10, GuiRenderHelper.COLOR_TEXT_SECONDARY);
            GuiRenderHelper.fill(ctx, x + 12, y + 28, width - 24, 1, GuiRenderHelper.COLOR_BORDER);
        }

        int startY = title != null ? y + 36 : y + 8;
        int visibleCount = (height - (startY - y)) / itemHeight;

        for (int i = 0; i < Math.min(visibleCount, items.size()); i++) {
            int idx = i + scrollOffset;
            if (idx >= items.size()) break;

            SidebarItem item = items.get(idx);
            int itemY = startY + i * itemHeight;
            boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, x + 4, itemY, width - 8, itemHeight);
            boolean selected = idx == selectedIndex;

            int bgColor = selected ? GuiRenderHelper.COLOR_ACCENT : (hovered ? GuiRenderHelper.COLOR_BG_LIGHT : GuiRenderHelper.COLOR_BG_MEDIUM);
            if (selected) {
                GuiRenderHelper.fill(ctx, x, itemY, 3, itemHeight, GuiRenderHelper.COLOR_ACCENT);
            }
            GuiRenderHelper.fill(ctx, x + 4, itemY, width - 8, itemHeight, bgColor);

            if (item.icon != null) {
                GuiRenderHelper.drawText(ctx, item.icon, x + 12, itemY + 6, GuiRenderHelper.COLOR_TEXT_PRIMARY);
            }

            GuiRenderHelper.drawText(ctx, item.label, x + (item.icon != null ? 36 : 16), itemY + 6, GuiRenderHelper.COLOR_TEXT_PRIMARY);
            if (item.description != null) {
                GuiRenderHelper.drawText(ctx, item.description, x + (item.icon != null ? 36 : 16), itemY + 20, GuiRenderHelper.COLOR_TEXT_SECONDARY);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startY = title != null ? y + 36 : y + 8;
        int visibleCount = (height - (startY - y)) / itemHeight;

        for (int i = 0; i < Math.min(visibleCount, items.size()); i++) {
            int idx = i + scrollOffset;
            if (idx >= items.size()) break;

            int itemY = startY + i * itemHeight;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, x + 4, itemY, width - 8, itemHeight)) {
                selectedIndex = idx;
                if (onSelect != null) {
                    onSelect.accept(idx);
                }
                return true;
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) verticalAmount, items.size() - 1));
        return true;
    }

    public static class SidebarItem {
        public final String icon;
        public final String label;
        public final String description;

        public SidebarItem(String icon, String label, String description) {
            this.icon = icon;
            this.label = label;
            this.description = description;
        }
    }
}
