package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ModernSlider {
    private final int x, y, width, height;
    private float value;
    private float min, max;
    private String label;
    private boolean dragging = false;
    private Consumer<Float> onChange;

    public ModernSlider(int x, int y, int width, int height, String label, float min, float max, float defaultValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = (defaultValue - min) / (max - min);
    }

    public ModernSlider setOnChange(Consumer<Float> callback) {
        this.onChange = callback;
        return this;
    }

    public float getValue() {
        return min + value * (max - min);
    }

    public void setValue(float val) {
        this.value = (val - min) / (max - min);
    }

    private int getSliderX() {
        return x + 8 + (int) (value * (width - 24));
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (label != null) {
            GuiRenderHelper.drawText(ctx, label, x, y - 10, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        }

        int trackY = y + height / 2 - 2;
        GuiRenderHelper.fill(ctx, x + 8, trackY, width - 24, 4, GuiRenderHelper.COLOR_BG_LIGHT);

        int fillWidth = (int) (value * (width - 24));
        if (fillWidth > 0) {
            GuiRenderHelper.fill(ctx, x + 8, trackY, fillWidth, 4, GuiRenderHelper.COLOR_ACCENT);
        }

        int sx = getSliderX();
        int sy = y + height / 2 - 6;
        boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, sx - 3, sy, 12, 12);
        int sliderColor = hovered || dragging ? GuiRenderHelper.COLOR_ACCENT_HOVER : GuiRenderHelper.COLOR_ACCENT;
        GuiRenderHelper.fill(ctx, sx, sy, 8, 12, sliderColor);
        GuiRenderHelper.border(ctx, sx, sy, 8, 12, 1, GuiRenderHelper.COLOR_BORDER);

        String valStr = String.format("%.1f", getValue());
        GuiRenderHelper.drawText(ctx, valStr, x + width - GuiRenderHelper.textWidth(valStr) - 4, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int sx = getSliderX();
        if (GuiRenderHelper.isHovered(mouseX, mouseY, sx, y + height / 2 - 6, 8, 12)) {
            dragging = true;
            return true;
        }
        if (GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height)) {
            updateValue(mouseX);
            dragging = true;
            return true;
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }

    public void mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging) {
            updateValue(mouseX);
        }
    }

    private void updateValue(double mouseX) {
        float newVal = (float) (mouseX - (x + 8)) / (width - 24);
        value = MathHelper.clamp(newVal, 0f, 1f);
        if (onChange != null) {
            onChange.accept(getValue());
        }
    }
}
