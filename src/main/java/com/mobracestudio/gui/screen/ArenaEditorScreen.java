package com.mobracestudio.gui.screen;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.arena.*;
import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ArenaEditorScreen extends Screen {
    private static final int SIDEBAR_WIDTH = 200;
    private static final int TOOLBAR_HEIGHT = 50;
    private static final int PROPERTY_PANEL_WIDTH = 220;

    private final Arena arena;
    private ModernSidebar toolSidebar;
    private ModernToolbar toolToolbar;
    private ModernPropertyPanel propertyPanel;
    private ModernTabPanel viewTabPanel;
    private ModernButton saveButton;
    private ModernButton backButton;

    private int currentTool = -1;

    public ArenaEditorScreen(Arena arena) {
        super(Text.literal("Editing: " + (arena.getName() != null ? arena.getName() : "Unnamed")));
        this.arena = arena;
    }

    @Override
    protected void init() {
        int sw = width;
        int sh = height;

        toolSidebar = new ModernSidebar(0, 0, SIDEBAR_WIDTH, sh - TOOLBAR_HEIGHT, "TOOLS");
        toolSidebar.addItem("\u2795", "Start", "Set start position");
        toolSidebar.addItem("\u2690", "Finish", "Set finish line");
        toolSidebar.addItem("\u2714", "Checkpoint", "Add checkpoint");
        toolSidebar.addItem("\uD83D\uDCCD", "Spawn", "Set mob spawn");
        toolSidebar.addItem("\u25A0", "Barrier", "Add barrier wall");
        toolSidebar.addItem("\u24C7", "Safe Zone", "Set safe zone");
        toolSidebar.addItem("\u21A9", "Respawn", "Set respawn point");
        toolSidebar.addItem("\u274C", "Delete", "Delete element");
        toolSidebar.addItem("\u2194", "Move", "Move element");
        toolSidebar.addItem("\u21BB", "Rotate", "Rotate element");
        toolSidebar.addItem("\uD83D\uDD04", "Duplicate", "Duplicate element");
        toolSidebar.setOnSelect(this::onToolSelected);

        toolToolbar = new ModernToolbar(0, sh - TOOLBAR_HEIGHT, sw, TOOLBAR_HEIGHT);
        toolToolbar.addTool("\uD83D\uDCBE", "Save Arena");
        toolToolbar.addTool("\u25B6", "Test Race");
        toolToolbar.addTool("\u23F9", "Stop");
        toolToolbar.addTool("\uD83D\uDD04", "Reset");
        toolToolbar.setOnToolSelect(this::onToolbarAction);

        propertyPanel = new ModernPropertyPanel(sw - PROPERTY_PANEL_WIDTH, 0, PROPERTY_PANEL_WIDTH, sh - TOOLBAR_HEIGHT);

        viewTabPanel = new ModernTabPanel(SIDEBAR_WIDTH, TOOLBAR_HEIGHT, sw - SIDEBAR_WIDTH - PROPERTY_PANEL_WIDTH, 32, 24);
        viewTabPanel.addTab("3D View");
        viewTabPanel.addTab("Top View");
        viewTabPanel.addTab("Side View");

        saveButton = new ModernButton(sw - 180, sh - TOOLBAR_HEIGHT + 8, 80, 34, "Save", b -> saveArena());
        backButton = new ModernButton(sw - 90, sh - TOOLBAR_HEIGHT + 8, 80, 34, "Back", b -> close());

        addDrawableChild(saveButton);
        addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        GuiRenderHelper.fill(ctx, 0, 0, width, height, GuiRenderHelper.COLOR_BG_DARK);

        toolSidebar.render(ctx, mouseX, mouseY, delta);
        toolToolbar.render(ctx, mouseX, mouseY, delta);
        propertyPanel.render(ctx, mouseX, mouseY, delta);
        viewTabPanel.render(ctx, mouseX, mouseY, delta);

        int viewX = SIDEBAR_WIDTH;
        int viewY = TOOLBAR_HEIGHT + 30;
        int viewW = width - SIDEBAR_WIDTH - PROPERTY_PANEL_WIDTH;
        int viewH = height - TOOLBAR_HEIGHT - viewY;

        GuiRenderHelper.fill(ctx, viewX, viewY, viewW, viewH, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, viewX, viewY, viewW, viewH, 1, GuiRenderHelper.COLOR_BORDER);

        GuiRenderHelper.drawTextCentered(ctx, "Arena Preview Area", viewX + viewW / 2, viewY + viewH / 2 - 10, GuiRenderHelper.COLOR_TEXT_DISABLED);
        renderArenaInfo(ctx, viewX + 10, viewY + 10);

        super.render(ctx, mouseX, mouseY, delta);

        renderStatusBar(ctx);
    }

    private void renderArenaInfo(DrawContext ctx, int x, int y) {
        GuiRenderHelper.drawText(ctx, "Arena: " + (arena.getName() != null ? arena.getName() : "Unnamed"), x, y, GuiRenderHelper.COLOR_ACCENT);
        GuiRenderHelper.drawText(ctx, "Checkpoints: " + arena.getTotalCheckpoints(), x, y + 14, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        GuiRenderHelper.drawText(ctx, "Spawn Points: " + arena.getSpawnPoints().size(), x, y + 28, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        GuiRenderHelper.drawText(ctx, "Barriers: " + arena.getBarriers().size(), x, y + 42, GuiRenderHelper.COLOR_TEXT_SECONDARY);
        GuiRenderHelper.drawText(ctx, "Laps: " + arena.getLaps(), x, y + 56, GuiRenderHelper.COLOR_TEXT_SECONDARY);

        if (arena.getStartPosition() != null) {
            var sp = arena.getStartPosition();
            GuiRenderHelper.drawText(ctx, "Start: " + formatVec(sp), x, y + 76, GuiRenderHelper.COLOR_SUCCESS);
        }
        if (arena.getFinishPosition() != null) {
            var fp = arena.getFinishPosition();
            GuiRenderHelper.drawText(ctx, "Finish: " + formatVec(fp), x, y + 90, GuiRenderHelper.COLOR_ERROR);
        }
    }

    private String formatVec(net.minecraft.util.math.Vec3d v) {
        return String.format("%.0f, %.0f, %.0f", v.x, v.y, v.z);
    }

    private void renderStatusBar(DrawContext ctx) {
        int sbY = height - TOOLBAR_HEIGHT;
        GuiRenderHelper.fill(ctx, SIDEBAR_WIDTH, sbY, width - SIDEBAR_WIDTH, 1, GuiRenderHelper.COLOR_BORDER);

        String status = currentTool >= 0 && toolSidebar.getSelectedItem() != null
            ? "Tool: " + toolSidebar.getSelectedItem().label
            : "Select a tool to start editing";
        GuiRenderHelper.drawText(ctx, status, SIDEBAR_WIDTH + 12, sbY + 16, GuiRenderHelper.COLOR_TEXT_SECONDARY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        toolSidebar.mouseClicked(mouseX, mouseY, button);
        toolToolbar.mouseClicked(mouseX, mouseY, button);
        propertyPanel.mouseClicked(mouseX, mouseY, button);
        viewTabPanel.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        toolSidebar.mouseScrolled(mouseX, mouseY, verticalAmount);
        propertyPanel.mouseScrolled(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, verticalAmount);
    }

    private void onToolSelected(int index) {
        this.currentTool = index;
        updatePropertyPanel();
    }

    private void onToolbarAction(int index) {
        switch (index) {
            case 0 -> saveArena();
            case 1 -> testRace();
            case 2 -> stopRace();
            case 3 -> resetArena();
        }
    }

    private void updatePropertyPanel() {
        // property panel groups are recreated each time
        if (currentTool < 0) return;

        var propGroup = propertyPanel.addGroup("Tool Settings");
        propGroup.add("Selected Tool", currentTool >= 0 ? toolSidebar.getSelectedItem().label : "None");
        propGroup.add("Arena", arena.getName());

        var infoGroup = propertyPanel.addGroup("Arena Info");
        infoGroup.add("Checkpoints", String.valueOf(arena.getTotalCheckpoints()));
        infoGroup.add("Spawn Points", String.valueOf(arena.getSpawnPoints().size()));
        infoGroup.add("Barriers", String.valueOf(arena.getBarriers().size()));
    }

    private void saveArena() {
        var server = MobRaceStudio.getInstance().getServer();
        if (server != null) {
            MobRaceStudio.getInstance().getStorageManager().saveArena(arena, server);
        }
    }

    private void testRace() {
        MobRaceStudio.LOGGER.info("Starting test race for arena: {}", arena.getName());
    }

    private void stopRace() {
        MobRaceStudio.LOGGER.info("Stopping race for arena: {}", arena.getName());
    }

    private void resetArena() {
        MobRaceStudio.LOGGER.info("Resetting arena: {}", arena.getName());
    }
}
