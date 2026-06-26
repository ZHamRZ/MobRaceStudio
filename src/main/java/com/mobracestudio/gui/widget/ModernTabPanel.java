package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernTabPanel {
    private final int x, y, width, height;
    private final int tabHeight;
    private final List<Tab> tabs = new ArrayList<>();
    private int selectedTab = 0;
    private Consumer<Integer> onTabChange;

    public ModernTabPanel(int x, int y, int width, int height, int tabHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tabHeight = tabHeight;
    }

    public ModernTabPanel addTab(String title) {
        tabs.add(new Tab(title));
        return this;
    }

    public ModernTabPanel setOnTabChange(Consumer<Integer> callback) {
        this.onTabChange = callback;
        return this;
    }

    public void selectTab(int index) {
        if (index >= 0 && index < tabs.size()) {
            this.selectedTab = index;
        }
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        int tabWidth = width / tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            int tx = x + i * tabWidth;
            boolean selected = i == selectedTab;
            boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, tx, y, tabWidth, tabHeight);

            if (selected) {
                GuiRenderHelper.fill(ctx, tx, y, tabWidth, tabHeight, GuiRenderHelper.COLOR_BG_DARK);
                GuiRenderHelper.fill(ctx, tx, y + tabHeight - 2, tabWidth, 2, GuiRenderHelper.COLOR_ACCENT);
            } else if (hovered) {
                GuiRenderHelper.fill(ctx, tx, y, tabWidth, tabHeight, GuiRenderHelper.COLOR_BG_LIGHT);
            } else {
                GuiRenderHelper.fill(ctx, tx, y, tabWidth, tabHeight - 1, GuiRenderHelper.COLOR_BG_MEDIUM);
            }

            if (i < tabs.size() - 1) {
                GuiRenderHelper.fill(ctx, tx + tabWidth - 1, y + 4, 1, tabHeight - 8, GuiRenderHelper.COLOR_BORDER);
            }

            GuiRenderHelper.drawTextCentered(ctx, tabs.get(i).title, tx + tabWidth / 2, y + tabHeight / 2 - 4,
                selected ? GuiRenderHelper.COLOR_ACCENT : GuiRenderHelper.COLOR_TEXT_SECONDARY);
        }

        int contentY = y + tabHeight + 4;
        int contentH = height - tabHeight - 5;
        GuiRenderHelper.fill(ctx, x + 1, contentY, width - 2, contentH, GuiRenderHelper.COLOR_BG_DARK);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int tabWidth = width / tabs.size();
        for (int i = 0; i < tabs.size(); i++) {
            int tx = x + i * tabWidth;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, tx, y, tabWidth, tabHeight)) {
                if (selectedTab != i) {
                    selectedTab = i;
                    if (onTabChange != null) {
                        onTabChange.accept(i);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private static class Tab {
        final String title;
        Tab(String title) {
            this.title = title;
        }
    }
}
