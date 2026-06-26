package com.mobracestudio.gui.screen;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class NameInputScreen extends Screen {
    private final Consumer<String> callback;
    private TextFieldWidget textField;

    public NameInputScreen(Consumer<String> callback) {
        super(Text.literal("Enter Name"));
        this.callback = callback;
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int cy = height / 2;

        textField = new TextFieldWidget(textRenderer, cx - 100, cy - 16, 200, 32, Text.literal("Name"));
        textField.setMaxLength(64);
        textField.setFocused(true);

        addDrawableChild(textField);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        ctx.fill(0, 0, width, height, 0x80000000);

        int cx = width / 2;
        int cy = height / 2;

        GuiRenderHelper.drawText(ctx, "ENTER NAME", cx - GuiRenderHelper.textWidth("ENTER NAME") / 2, cy - 50, GuiRenderHelper.COLOR_ACCENT);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 335) {
            String name = textField.getText().trim();
            if (!name.isEmpty()) {
                callback.accept(name);
                client.setScreen(null);
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
