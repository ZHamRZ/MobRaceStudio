package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ModernSearchBar {
    private final int x, y, width, height;
    private StringBuffer text = new StringBuffer();
    private boolean focused = false;
    private int cursorPos = 0;
    private float cursorTimer = 0;
    private Consumer<String> onChange;
    private String placeholder;

    public ModernSearchBar(int x, int y, int width, int height, String placeholder) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
    }

    public ModernSearchBar setOnChange(Consumer<String> callback) {
        this.onChange = callback;
        return this;
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String t) {
        text = new StringBuffer(t);
        cursorPos = text.length();
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        cursorTimer += delta;
        boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height);
        int bgColor = focused ? GuiRenderHelper.COLOR_BG_LIGHT : GuiRenderHelper.COLOR_BG_MEDIUM;

        GuiRenderHelper.fill(ctx, x, y, width, height, bgColor);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, focused ? GuiRenderHelper.COLOR_ACCENT : GuiRenderHelper.COLOR_BORDER);

        GuiRenderHelper.drawText(ctx, "\u2315", x + 6, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_SECONDARY);

        String displayText = text.toString();
        int maxTextWidth = width - 40;
        int textStart = x + 24;

        if (displayText.isEmpty() && !focused && placeholder != null) {
            GuiRenderHelper.drawText(ctx, placeholder, textStart, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_DISABLED);
        } else {
            GuiRenderHelper.drawText(ctx, displayText, textStart, y + height / 2 - 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);
        }

        if (focused && (int) (cursorTimer * 3) % 2 == 0) {
            int cx = textStart + GuiRenderHelper.textWidth(displayText.substring(0, Math.min(cursorPos, displayText.length())));
            GuiRenderHelper.fill(ctx, cx, y + 4, 1, height - 8, GuiRenderHelper.COLOR_TEXT_PRIMARY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        focused = GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height);
        if (focused) {
            cursorPos = text.length();
        }
        return focused;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        if (keyCode == 259) {
            if (cursorPos > 0 && text.length() > 0) {
                text.deleteCharAt(cursorPos - 1);
                cursorPos = Math.max(0, cursorPos - 1);
                notifyChange();
            }
            return true;
        }
        if (keyCode == 257 || keyCode == 335) {
            focused = false;
            return true;
        }
        if (keyCode == 263) {
            cursorPos = Math.max(0, cursorPos - 1);
            return true;
        }
        if (keyCode == 262) {
            cursorPos = Math.min(text.length(), cursorPos + 1);
            return true;
        }
        if (keyCode == 268) {
            cursorPos = 0;
            return true;
        }
        if (keyCode == 269) {
            cursorPos = text.length();
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!focused) return false;
        if (chr >= ' ' && chr <= '~' || chr > 255) {
            text.insert(cursorPos, chr);
            cursorPos++;
            notifyChange();
            return true;
        }
        return false;
    }

    private void notifyChange() {
        if (onChange != null) {
            onChange.accept(text.toString());
        }
    }

    public boolean isFocused() {
        return focused;
    }
}
