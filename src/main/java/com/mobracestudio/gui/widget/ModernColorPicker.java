package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ModernColorPicker {
    private final int x, y, width, height;
    private float hue;
    private float saturation;
    private float brightness;
    private boolean expanded;
    private Consumer<Integer> onChange;

    public ModernColorPicker(int x, int y, int width, int height, int defaultColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float[] hsb = java.awt.Color.RGBtoHSB(
            (defaultColor >> 16) & 0xFF,
            (defaultColor >> 8) & 0xFF,
            defaultColor & 0xFF, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    public ModernColorPicker setOnChange(Consumer<Integer> callback) {
        this.onChange = callback;
        return this;
    }

    public int getColor() {
        return java.awt.Color.HSBtoRGB(hue, saturation, brightness) | 0xFF000000;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        GuiRenderHelper.fill(ctx, x, y, width, height, getColor());
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        if (expanded) {
            int pickerH = 150;
            GuiRenderHelper.fill(ctx, x, y + height, width, pickerH, GuiRenderHelper.COLOR_BG_DARK);
            GuiRenderHelper.border(ctx, x, y + height, width, pickerH, 1, GuiRenderHelper.COLOR_BORDER);

            for (int i = 0; i < width; i++) {
                float h = (float) i / width;
                int color = java.awt.Color.HSBtoRGB(h, 1.0f, 1.0f) | 0xFF000000;
                GuiRenderHelper.fill(ctx, x + i, y + height + 10, 1, 12, color);
            }

            int sx = x + (int) (hue * width);
            ctx.fill(sx - 2, y + height + 8, 4, 16, 0xFFFFFFFF);
            ctx.fill(sx - 1, y + height + 9, 2, 14, 0xFF000000);

            for (int i = 0; i < 80; i++) {
                float s = (float) i / 80;
                for (int j = 0; j < 80; j++) {
                    float b = 1f - (float) j / 80;
                    int color = java.awt.Color.HSBtoRGB(hue, s, b) | 0xFF000000;
                    GuiRenderHelper.fill(ctx, x + i + 10, y + height + 30 + j, 1, 1, color);
                }
            }

            int sx2 = x + 10 + (int) (saturation * 80);
            int sy2 = y + height + 30 + (int) ((1 - brightness) * 80);
            ctx.fill(sx2 - 3, sy2 - 3, 6, 6, 0xFFFFFFFF);
            ctx.fill(sx2 - 2, sy2 - 2, 4, 4, 0xFF000000);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height)) {
            expanded = !expanded;
            return true;
        }

        if (expanded) {
            int pickerH = 150;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, x, y + height, width, pickerH)) {
                double localX = mouseX - x;
                double localY = mouseY - (y + height);

                if (localY >= 10 && localY < 22) {
                    hue = MathHelper.clamp((float) (localX / width), 0, 1);
                }

                if (localY >= 30 && localY < 110) {
                    saturation = MathHelper.clamp((float) ((localX - 10) / 80), 0, 1);
                    brightness = MathHelper.clamp(1f - (float) ((localY - 30) / 80), 0, 1);
                }

                if (onChange != null) {
                    onChange.accept(getColor());
                }
                return true;
            }
            expanded = false;
        }
        return false;
    }
}
