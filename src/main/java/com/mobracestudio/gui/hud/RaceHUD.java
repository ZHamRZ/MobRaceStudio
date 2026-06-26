package com.mobracestudio.gui.hud;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class RaceHUD {
    private boolean visible = false;
    private int position = 0;
    private int totalRacers = 0;
    private int currentLap = 0;
    private int totalLaps = 0;
    private int currentCheckpoint = 0;
    private int totalCheckpoints = 0;
    private float speed = 0;
    private float raceTime = 0;
    private String rank = "-";

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void updateRaceData(int pos, int total, int lap, int maxLaps, int cp, int maxCp, float spd, float time, String r) {
        this.position = pos;
        this.totalRacers = total;
        this.currentLap = lap;
        this.totalLaps = maxLaps;
        this.currentCheckpoint = cp;
        this.totalCheckpoints = maxCp;
        this.speed = spd;
        this.raceTime = time;
        this.rank = r;
    }

    public void render(DrawContext ctx, float tickDelta) {
        if (!visible) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int sw = ctx.getScaledWindowWidth();
        int sh = ctx.getScaledWindowHeight();

        int hudX = sw - 210;
        int hudY = 10;
        int hudW = 200;
        int hudH = 130;

        GuiRenderHelper.fill(ctx, hudX, hudY, hudW, hudH, 0xCC1A1B2F);
        GuiRenderHelper.border(ctx, hudX, hudY, hudW, hudH, 1, GuiRenderHelper.COLOR_BORDER);
        GuiRenderHelper.fill(ctx, hudX, hudY + 20, hudW, 1, GuiRenderHelper.COLOR_BORDER);

        GuiRenderHelper.drawText(ctx, "MOB RACE", hudX + 8, hudY + 5, GuiRenderHelper.COLOR_ACCENT);

        int lineY = hudY + 28;
        int labelX = hudX + 8;
        int valueX = hudX + hudW - 8;

        drawHudLine(ctx, "Position", position + "/" + totalRacers, labelX, valueX, lineY);
        drawHudLine(ctx, "Lap", currentLap + "/" + totalLaps, labelX, valueX, lineY += 18);
        drawHudLine(ctx, "Checkpoint", currentCheckpoint + "/" + totalCheckpoints, labelX, valueX, lineY += 18);
        drawHudLine(ctx, "Speed", String.format("%.1f", speed), labelX, valueX, lineY += 18);
        drawHudLine(ctx, "Time", formatTime(raceTime), labelX, valueX, lineY += 18);
        drawHudLine(ctx, "Rank", rank, labelX, valueX, lineY += 18);
    }

    private void drawHudLine(DrawContext ctx, String label, String value, int labelX, int valueX, int y) {
        GuiRenderHelper.drawText(ctx, label, labelX, y, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        int vw = GuiRenderHelper.textWidth(value);
        GuiRenderHelper.drawText(ctx, value, valueX - vw, y, GuiRenderHelper.COLOR_TEXT_PRIMARY);
    }

    private String formatTime(float seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        int millis = (int) ((seconds - (int) seconds) * 100);
        return String.format("%02d:%02d.%02d", mins, secs, millis);
    }
}
