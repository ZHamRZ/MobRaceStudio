package com.mobracestudio.gui.screen;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class RaceControlScreen extends Screen {
    private static final int PANEL_WIDTH = 280;

    private final String arenaId;
    private ModernButton startButton;
    private ModernButton stopButton;
    private ModernButton resetButton;
    private ModernButton backButton;
    private ModernProgressBar raceProgress;
    private ModernList<String> leaderboardList;
    private ModernTabPanel infoTabs;

    private boolean raceRunning = false;
    private String raceStatus = "Ready";

    public RaceControlScreen(String arenaId) {
        super(Text.literal("Race Control"));
        this.arenaId = arenaId;
    }

    @Override
    protected void init() {
        int sh = height;
        int panelX = 12;
        int panelY = 12;
        int panelW = PANEL_WIDTH;
        int btnY = panelY + 100;

        startButton = new ModernButton(panelX + 16, btnY, panelW - 32, 32, "Start Race", b -> startRace());
        stopButton = new ModernButton(panelX + 16, btnY + 40, panelW - 32, 32, "Stop Race", b -> stopRace());
        stopButton.active = false;
        resetButton = new ModernButton(panelX + 16, btnY + 80, panelW - 32, 32, "Reset", b -> resetRace());
        backButton = new ModernButton(panelX + 16, sh - 44, panelW - 32, 32, "Back", b -> close());

        addDrawableChild(startButton);
        addDrawableChild(stopButton);
        addDrawableChild(resetButton);
        addDrawableChild(backButton);

        raceProgress = new ModernProgressBar(panelX + 16, btnY + 130, panelW - 32, 20, "Race Progress");

        leaderboardList = new ModernList<>(panelX + 16, btnY + 200, panelW - 32, 120, 20);

        infoTabs = new ModernTabPanel(PANEL_WIDTH + 16, 12, width - PANEL_WIDTH - 28, height - 24, 24);
        infoTabs.addTab("Stats");
        infoTabs.addTab("Mobs");
        infoTabs.addTab("Log");
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        GuiRenderHelper.fill(ctx, 0, 0, width, height, GuiRenderHelper.COLOR_BG_DARK);

        renderControlPanel(ctx);
        raceProgress.render(ctx, mouseX, mouseY, delta);
        leaderboardList.render(ctx, mouseX, mouseY, delta);
        infoTabs.render(ctx, mouseX, mouseY, delta);

        renderArenaPreview(ctx);

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void renderControlPanel(DrawContext ctx) {
        int panelX = 12;
        int panelY = 12;
        int panelW = PANEL_WIDTH;

        GuiRenderHelper.fill(ctx, panelX, panelY, panelW, 80, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, panelX, panelY, panelW, 80, 1, GuiRenderHelper.COLOR_BORDER);

        var arena = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getArena(arenaId);
        String arenaName = arena != null ? arena.getName() : "Unknown";

        GuiRenderHelper.drawText(ctx, "Race Control", panelX + 16, panelY + 10, GuiRenderHelper.COLOR_ACCENT);
        GuiRenderHelper.drawText(ctx, "Arena: " + arenaName, panelX + 16, panelY + 28, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        GuiRenderHelper.drawText(ctx, "Status: " + raceStatus, panelX + 16, panelY + 46, raceRunning ? GuiRenderHelper.COLOR_SUCCESS : GuiRenderHelper.COLOR_TEXT_SECONDARY);
    }

    private void renderArenaPreview(DrawContext ctx) {
        int px = PANEL_WIDTH + 16;
        int py = 150;
        int pw = width - px - 12;
        int ph = height - py - 12;

        GuiRenderHelper.fill(ctx, px, py, pw, ph, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, px, py, pw, ph, 1, GuiRenderHelper.COLOR_BORDER);
        GuiRenderHelper.drawTextCentered(ctx, "Arena Preview", px + pw / 2, py + ph / 2, GuiRenderHelper.COLOR_TEXT_DISABLED);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        leaderboardList.mouseClicked(mouseX, mouseY, button);
        infoTabs.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        leaderboardList.mouseScrolled(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, verticalAmount);
    }

    private void startRace() {
        raceRunning = true;
        raceStatus = "Running";
        startButton.active = false;
        stopButton.active = true;
    }

    private void stopRace() {
        raceRunning = false;
        raceStatus = "Stopped";
        startButton.active = true;
        stopButton.active = false;
    }

    private void resetRace() {
        raceRunning = false;
        raceStatus = "Ready";
        startButton.active = true;
        stopButton.active = false;
        raceProgress.setProgress(0);
    }
}
