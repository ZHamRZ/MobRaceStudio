package com.mobracestudio.gui.widget;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

public class ModernCheckbox {
    private final int x, y, size;
    private boolean checked;
    private String label;
    private Consumer<Boolean> onChange;

    public ModernCheckbox(int x, int y, int size, String label, boolean defaultChecked) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
        this.checked = defaultChecked;
    }

    public ModernCheckbox setOnChange(Consumer<Boolean> callback) {
        this.onChange = callback;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        boolean hovered = GuiRenderHelper.isHovered(mouseX, mouseY, x, y, size, size);

        int boxColor = checked ? GuiRenderHelper.COLOR_ACCENT : (hovered ? GuiRenderHelper.COLOR_BG_LIGHT : GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.fill(ctx, x, y, size, size, boxColor);
        GuiRenderHelper.border(ctx, x, y, size, size, 1, checked ? GuiRenderHelper.COLOR_ACCENT_HOVER : GuiRenderHelper.COLOR_BORDER);

        if (checked) {
            GuiRenderHelper.drawTextCentered(ctx, "\u2714", x + size / 2, y + size / 2, GuiRenderHelper.COLOR_TEXT_PRIMARY);
        }

        if (label != null) {
            GuiRenderHelper.drawText(ctx, label, x + size + 8, y + size / 2 - 4, GuiRenderHelper.COLOR_TEXT_PRIMARY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (GuiRenderHelper.isHovered(mouseX, mouseY, x, y, size, size)) {
            checked = !checked;
            if (onChange != null) {
                onChange.accept(checked);
            }
            return true;
        }
        if (label != null) {
            int lw = GuiRenderHelper.textWidth(label);
            if (GuiRenderHelper.isHovered(mouseX, mouseY, x + size + 8, y, lw, size)) {
                checked = !checked;
                if (onChange != null) {
                    onChange.accept(checked);
                }
                return true;
            }
        }
        return false;
    }
}
