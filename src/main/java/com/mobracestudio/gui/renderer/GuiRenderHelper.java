package com.mobracestudio.gui.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GuiRenderHelper {
    public static final int COLOR_BG_DARK = 0xFF1A1B2F;
    public static final int COLOR_BG_MEDIUM = 0xFF252640;
    public static final int COLOR_BG_LIGHT = 0xFF2D2E4A;
    public static final int COLOR_ACCENT = 0xFF6C63FF;
    public static final int COLOR_ACCENT_HOVER = 0xFF7B73FF;
    public static final int COLOR_ACCENT_PRESSED = 0xFF5A52E0;
    public static final int COLOR_TEXT_PRIMARY = 0xFFE8E8F0;
    public static final int COLOR_TEXT_SECONDARY = 0xFFA0A0B8;
    public static final int COLOR_TEXT_DISABLED = 0xFF606078;
    public static final int COLOR_BORDER = 0xFF3D3E5C;
    public static final int COLOR_SUCCESS = 0xFF4CAF50;
    public static final int COLOR_WARNING = 0xFFFFC107;
    public static final int COLOR_ERROR = 0xFFF44336;
    public static final int COLOR_INFO = 0xFF2196F3;

    public static void fill(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, w, h, color);
    }

    public static void fillGradientVertical(DrawContext ctx, int x, int y, int w, int h, int colorTop, int colorBottom) {
        for (int i = 0; i < h; i++) {
            float ratio = (float) i / h;
            ctx.fill(x, y + i, w, 1, lerpColor(colorTop, colorBottom, ratio));
        }
    }

    public static void border(DrawContext ctx, int x, int y, int w, int h, int size, int color) {
        ctx.fill(x, y, w, size, color);
        ctx.fill(x, y + h - size, w, size, color);
        ctx.fill(x, y + size, size, h - size * 2, color);
        ctx.fill(x + w - size, y + size, size, h - size * 2, color);
    }

    public static void drawText(DrawContext ctx, String text, int x, int y, int color) {
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }

    public static void drawTextCentered(DrawContext ctx, String text, int x, int y, int color) {
        int w = MinecraftClient.getInstance().textRenderer.getWidth(text);
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, x - w / 2, y - 4, color, true);
    }

    public static int textWidth(String text) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    public static int lerpColor(int c1, int c2, float t) {
        int a = (int) (((c1 >> 24) & 0xFF) * (1 - t) + ((c2 >> 24) & 0xFF) * t);
        int r = (int) (((c1 >> 16) & 0xFF) * (1 - t) + ((c2 >> 16) & 0xFF) * t);
        int g = (int) (((c1 >> 8) & 0xFF) * (1 - t) + ((c2 >> 8) & 0xFF) * t);
        int b = (int) ((c1 & 0xFF) * (1 - t) + (c2 & 0xFF) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darken(int color, float factor) {
        int r = (int) (((color >> 16) & 0xFF) * factor);
        int g = (int) (((color >> 8) & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    public static int brighten(int color, float factor) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    public static boolean isHovered(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }
}
