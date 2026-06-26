package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

public class ModernProgressBar {
    private final int x, y, width, height;
    private float progress;
    private String label;
    private int barColor = GuiRenderHelper.COLOR_ACCENT;
    private boolean showLabel = true;

    public ModernProgressBar(int x, int y, int width, int height, String label) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
    }

    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(1, progress));
    }

    public float getProgress() {
        return progress;
    }

    public void setBarColor(int color) {
        this.barColor = color;
    }

    public void setShowLabel(boolean show) {
        this.showLabel = show;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_LIGHT);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        int fillWidth = (int) (progress * (width - 4));
        if (fillWidth > 0) {
            GuiRenderHelper.fill(ctx, x + 2, y + 2, fillWidth, height - 4, barColor);
        }

        if (showLabel) {
            String text = label != null ? label + ": " + (int) (progress * 100) + "%" : (int) (progress * 100) + "%";
            GuiRenderHelper.drawTextCentered(ctx, text, x + width / 2, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);
        }
    }
}
