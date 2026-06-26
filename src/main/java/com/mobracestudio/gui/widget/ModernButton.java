package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ModernButton extends ClickableWidget {
    private boolean pressed = false;
    private static final int CORNER_RADIUS = 4;
    private static final int ANIMATION_SPEED = 8;

    private int backgroundColor = GuiRenderHelper.COLOR_ACCENT;
    private int hoverColor = GuiRenderHelper.COLOR_ACCENT_HOVER;
    private int pressedColor = GuiRenderHelper.COLOR_ACCENT_PRESSED;
    private int textColor = GuiRenderHelper.COLOR_TEXT_PRIMARY;
    private int borderColor = 0;
    private int borderSize = 0;

    private Consumer<ModernButton> onClick;
    private float hoverAnim = 0f;
    private boolean iconMode = false;
    private String iconText = "";

    public ModernButton(int x, int y, int width, int height, String text, Consumer<ModernButton> onClick) {
        super(x, y, width, height, Text.literal(text));
        this.onClick = onClick;
    }

    public ModernButton setColors(int bg, int hover, int pressed) {
        this.backgroundColor = bg;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        return this;
    }

    public ModernButton setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public ModernButton setBorder(int size, int color) {
        this.borderSize = size;
        this.borderColor = color;
        return this;
    }

    public ModernButton setIcon(String icon) {
        this.iconMode = true;
        this.iconText = icon;
        return this;
    }

    @Override
    public void renderButton(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.hoverAnim = MathHelper.lerp(delta * ANIMATION_SPEED * 0.1f, hoverAnim, isSelected() ? 1f : 0f);

        int color;
        if (!this.active) {
            color = darkenColor(backgroundColor, 0.5f);
        } else if (this.pressed) {
            color = pressedColor;
        } else if (isHovered()) {
            color = hoverColor;
        } else {
            color = backgroundColor;
        }

        int animColor = GuiRenderHelper.lerpColor(color, brightenColor(color, 1.1f), hoverAnim);

        if (borderSize > 0) {
            GuiRenderHelper.border(ctx, getX(), getY(), getWidth(), getHeight(), borderSize, borderColor);
        }

        GuiRenderHelper.fill(ctx, getX(), getY(), getWidth(), getHeight(), animColor);

        int textX = getX() + getWidth() / 2;
        int textY = getY() + getHeight() / 2;
        String displayText = iconMode ? iconText : getMessage().getString();
        GuiRenderHelper.drawTextCentered(ctx, displayText, textX, textY, textColor);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (onClick != null && this.active) {
            onClick.accept(this);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.active && this.isHovered()) {
            this.pressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.pressed) {
            this.pressed = false;
            if (this.isHovered()) {
                this.onClick(mouseX, mouseY);
            }
            return true;
        }
        return false;
    }

    @Override
    public void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {
    }

    private int darkenColor(int color, float factor) {
        int r = (int) (((color >> 16) & 0xFF) * factor);
        int g = (int) (((color >> 8) & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    private int brightenColor(int color, float factor) {
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }
}
