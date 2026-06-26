package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class ModernPopup {
    private final int x, y, width, height;
    private String title;
    private String message;
    private String confirmText;
    private String cancelText;
    private Consumer<Boolean> callback;
    private boolean visible = false;

    public ModernPopup(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void show(String title, String message, String confirmText, String cancelText, Consumer<Boolean> callback) {
        this.title = title;
        this.message = message;
        this.confirmText = confirmText;
        this.cancelText = cancelText;
        this.callback = callback;
        this.visible = true;
    }

    public void hide() {
        this.visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (!visible) return;

        ctx.fill(0, 0, ctx.getScaledWindowWidth(), ctx.getScaledWindowHeight(), 0x80000000);

        GuiRenderHelper.fill(ctx, x, y, width, height, GuiRenderHelper.COLOR_BG_DARK);
        GuiRenderHelper.border(ctx, x, y, width, height, 1, GuiRenderHelper.COLOR_BORDER);

        if (title != null) {
            GuiRenderHelper.drawTextCentered(ctx, title, x + width / 2, y + 16, GuiRenderHelper.COLOR_TEXT_PRIMARY);
            GuiRenderHelper.fill(ctx, x + 20, y + 32, width - 40, 1, GuiRenderHelper.COLOR_BORDER);
        }

        if (message != null) {
            int msgY = title != null ? y + 44 : y + 20;
            String[] lines = message.split("\n");
            for (int i = 0; i < lines.length; i++) {
                GuiRenderHelper.drawTextCentered(ctx, lines[i], x + width / 2, msgY + i * 14, GuiRenderHelper.COLOR_TEXT_SECONDARY);
            }
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible) return false;

        int btnY = y + height - 36;
        int btnW = 80;

        if (cancelText != null) {
            int cx = x + width / 2 + 6;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, cx, btnY, btnW, 24)) {
                visible = false;
                if (callback != null) callback.accept(false);
                return true;
            }
        }

        if (confirmText != null) {
            int cx = x + width / 2 - btnW - 6;
            if (GuiRenderHelper.isHovered(mouseX, mouseY, cx, btnY, btnW, 24)) {
                visible = false;
                if (callback != null) callback.accept(true);
                return true;
            }
        }

        if (!GuiRenderHelper.isHovered(mouseX, mouseY, x, y, width, height)) {
            visible = false;
            if (callback != null) callback.accept(false);
            return true;
        }

        return false;
    }
}
