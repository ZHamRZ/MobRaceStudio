package com.mobracestudio.gui.screen;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.arena.Arena;
import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.ModernButton;
import com.mobracestudio.gui.widget.ModernSlider;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CreateArenaScreen extends Screen {
    private static final int DIALOG_WIDTH = 320;
    private static final int DIALOG_HEIGHT = 240;

    private final Screen parent;
    private String arenaName = "";
    private boolean nameError = false;
    private ModernSlider lapSlider;
    private ModernButton confirmButton;
    private ModernButton cancelButton;
    private int dialogX, dialogY;

    public CreateArenaScreen(Screen parent) {
        super(Text.literal("Create Arena"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        dialogX = (width - DIALOG_WIDTH) / 2;
        dialogY = (height - DIALOG_HEIGHT) / 2;

        lapSlider = new ModernSlider(dialogX + 30, dialogY + 100, DIALOG_WIDTH - 60, 20, "Laps", 1, 10, 1);
        confirmButton = new ModernButton(dialogX + 20, dialogY + DIALOG_HEIGHT - 40, 130, 28, "Create Arena", b -> createArena());
        cancelButton = new ModernButton(dialogX + DIALOG_WIDTH - 150, dialogY + DIALOG_HEIGHT - 40, 130, 28, "Cancel", b -> close());

        addDrawableChild(confirmButton);
        addDrawableChild(cancelButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        ctx.fill(0, 0, width, height, 0x80000000);

        GuiRenderHelper.fill(ctx, dialogX, dialogY, DIALOG_WIDTH, DIALOG_HEIGHT, GuiRenderHelper.COLOR_BG_DARK);
        GuiRenderHelper.border(ctx, dialogX, dialogY, DIALOG_WIDTH, DIALOG_HEIGHT, 1, GuiRenderHelper.COLOR_BORDER);
        GuiRenderHelper.drawTextCentered(ctx, "CREATE NEW ARENA", dialogX + DIALOG_WIDTH / 2, dialogY + 16, GuiRenderHelper.COLOR_ACCENT);
        GuiRenderHelper.fill(ctx, dialogX + 30, dialogY + 30, DIALOG_WIDTH - 60, 1, GuiRenderHelper.COLOR_BORDER);

        GuiRenderHelper.drawText(ctx, "Arena Name:", dialogX + 30, dialogY + 44, GuiRenderHelper.COLOR_TEXT_SECONDARY);

        int nameBoxX = dialogX + 30;
        int nameBoxY = dialogY + 60;
        int nameBoxW = DIALOG_WIDTH - 60;
        int nameBoxH = 28;

        GuiRenderHelper.fill(ctx, nameBoxX, nameBoxY, nameBoxW, nameBoxH, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, nameBoxX, nameBoxY, nameBoxW, nameBoxH, 1,
            nameError ? GuiRenderHelper.COLOR_ERROR : GuiRenderHelper.COLOR_BORDER);

        String display = arenaName.isEmpty() ? "Enter arena name..." : arenaName;
        GuiRenderHelper.drawText(ctx, display, nameBoxX + 8, nameBoxY + 8,
            arenaName.isEmpty() ? GuiRenderHelper.COLOR_TEXT_DISABLED : GuiRenderHelper.COLOR_TEXT_PRIMARY);

        if (nameError) {
            GuiRenderHelper.drawText(ctx, "Name is required or already exists!", dialogX + 30, nameBoxY + nameBoxH + 4, GuiRenderHelper.COLOR_ERROR);
        }

        lapSlider.render(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int nameBoxX = dialogX + 30;
        int nameBoxY = dialogY + 60;
        int nameBoxW = DIALOG_WIDTH - 60;
        int nameBoxH = 28;

        if (GuiRenderHelper.isHovered(mouseX, mouseY, nameBoxX, nameBoxY, nameBoxW, nameBoxH)) {
            client.setScreen(new NameInputScreen(this::onNameSet));
            return true;
        }

        lapSlider.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        lapSlider.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        lapSlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void onNameSet(String name) {
        this.arenaName = name;
        this.nameError = false;
    }

    private void createArena() {
        if (arenaName.isEmpty()) {
            nameError = true;
            return;
        }

        var storage = MobRaceStudio.getInstance().getStorageManager();
        if (storage.getArenaManager().containsArena(arenaName)) {
            nameError = true;
            return;
        }

        Arena arena = new Arena(arenaName);
        arena.setLaps((int) lapSlider.getValue());

        var server = MobRaceStudio.getInstance().getServer();
        if (server != null) {
            storage.saveArena(arena, server);
        } else {
            storage.getArenaManager().addArena(arena);
        }

        close();
    }

    public void close() {
        client.setScreen(parent);
    }
}
