package com.mobracestudio.util;

public class ColorHelper {
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int rgba(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int hex(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() == 6) hex = "FF" + hex;
        return (int) Long.parseLong(hex, 16);
    }

    public static int alpha(int color) {
        return (color >> 24) & 0xFF;
    }

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    public static int blue(int color) {
        return color & 0xFF;
    }

    public static int multiply(int color, float factor) {
        int a = alpha(color);
        int r = (int) (red(color) * factor);
        int g = (int) (green(color) * factor);
        int b = (int) (blue(color) * factor);
        return argb(Math.min(255, a), Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }
}
