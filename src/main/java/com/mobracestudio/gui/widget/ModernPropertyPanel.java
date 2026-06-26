package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModernPropertyPanel {
    private final int x, y, width, height;
    private final List<PropertyGroup> groups = new ArrayList<>();
    private int scrollOffset = 0;

    public ModernPropertyPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public PropertyGroup addGroup(String title) {
        PropertyGroup group = new PropertyGroup(title);
        groups.add(group);
        return group;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.fill(ctx, x, y, 1, height, GuiRenderHelper.COLOR_BORDER);

        int currentY = y + 8 - scrollOffset;

        for (PropertyGroup group : groups) {
            if (currentY > y + height) break;

            int groupEnd = currentY + 24 + group.properties.size() * 28;
            if (groupEnd > y) {
                GuiRenderHelper.fill(ctx, x + 8, currentY, width - 16, 20, GuiRenderHelper.COLOR_BG_LIGHT);
                GuiRenderHelper.drawText(ctx, group.title, x + 12, currentY + 4, GuiRenderHelper.COLOR_ACCENT);

                int propY = currentY + 24;
                for (Property prop : group.properties) {
                    GuiRenderHelper.drawText(ctx, prop.label, x + 12, propY + 4, GuiRenderHelper.COLOR_TEXT_SECONDARY);

                    if (prop.value != null) {
                        GuiRenderHelper.drawText(ctx, prop.value, x + width - 12 - GuiRenderHelper.textWidth(prop.value), propY + 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);
                    }
                    propY += 28;
                }
            }

            currentY = groupEnd + 8;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        int totalHeight = groups.size() * 24;
        for (PropertyGroup g : groups) {
            totalHeight += g.properties.size() * 28;
        }
        scrollOffset = Math.max(0, Math.min(scrollOffset - (int) verticalAmount * 20, totalHeight - height + 40));
        return true;
    }

    public static class PropertyGroup {
        public final String title;
        public final List<Property> properties = new ArrayList<>();

        public PropertyGroup(String title) {
            this.title = title;
        }

        public PropertyGroup add(String label, String value) {
            properties.add(new Property(label, value));
            return this;
        }
    }

    public static class Property {
        public final String label;
        public String value;

        public Property(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }
}
