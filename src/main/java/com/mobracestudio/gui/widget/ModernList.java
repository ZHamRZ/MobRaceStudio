package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernList<T> {
    private final int x, y, width, height;
    private final int itemHeight;
    private final List<ListItem<T>> items = new ArrayList<>();
    private int selectedIndex = -1;
    private int scrollOffset = 0;
    private Consumer<Integer> onSelect;
    private Consumer<Integer> onDoubleClick;

    public ModernList(int x, int y, int width, int height, int itemHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.itemHeight = itemHeight;
    }

    public void setItems(List<ListItem<T>> newItems) {
        items.clear();
        items.addAll(newItems);
        selectedIndex = -1;
        scrollOffset = 0;
    }

    public void addItem(ListItem<T> item) {
        items.add(item);
    }

    public void clear() {
        items.clear();
        selectedIndex = -1;
        scrollOffset = 0;
    }

    public ModernList<T> setOnSelect(Consumer<Integer> callback) {
        this.onSelect = callback;
        return this;
    }

    public ModernList<T> setOnDoubleClick(Consumer<Integer> callback) {
        this.onDoubleClick = callback;
        return this;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public ListItem<T> getSelectedItem() {
        return selectedIndex >= 0 && selectedIndex < items.size() ? items.get(selectedIndex) : null;
    }

    public T getSelectedValue() {
        ListItem<T> item = getSelectedItem();
        return item != null ? item.value : null;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        int startY = y + 2;
        int visibleCount = (height - 4) / itemHeight;

        for (int i = 0; i < visibleCount && (i + scrollOffset) < items.size(); i++) {
            int idx = i + scrollOffset;
            ListItem<T> item = items.get(idx);
            int iy = startY + i * itemHeight;
            boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, x + 2, iy, width - 4, itemHeight);
            boolean selected = idx == selectedIndex;

            if (selected) {
                GuiRenderHelper.fill(ctx, x + 2, iy, width - 4, itemHeight, GuiRenderHelper.COLOR_ACCENT);
            } else if (hovered) {
                GuiRenderHelper.fill(ctx, x + 2, iy, width - 4, itemHeight, GuiRenderHelper.COLOR_BG_LIGHT);
            }

            GuiRenderHelper.drawText(ctx, item.label, x + 10, iy + itemHeight / 2 - 4,
                selected ? GuiRenderHelper.COLOR_TEXT_PRIMARY :
                hovered ? GuiRenderHelper.COLOR_TEXT_PRIMARY : GuiRenderHelper.COLOR_TEXT_SECONDARY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startY = y + 2;
        int visibleCount = (height - 4) / itemHeight;

        for (int i = 0; i < visibleCount && (i + scrollOffset) < items.size(); i++) {
            int idx = i + scrollOffset;
            int iy = startY + i * itemHeight;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, x + 2, iy, width - 4, itemHeight)) {
                if (idx == selectedIndex && onDoubleClick != null) {
                    onDoubleClick.accept(idx);
                } else {
                    selectedIndex = idx;
                    if (onSelect != null) {
                        onSelect.accept(idx);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        int maxScroll = Math.max(0, items.size() - (height - 4) / itemHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) verticalAmount, maxScroll));
        return true;
    }

    public static class ListItem<T> {
        public final String label;
        public final T value;

        public ListItem(String label, T value) {
            this.label = label;
            this.value = value;
        }
    }
}
