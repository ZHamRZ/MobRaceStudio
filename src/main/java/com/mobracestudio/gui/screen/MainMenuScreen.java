package com.mobracestudio.gui.screen;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.*;
import com.mobracestudio.MobRaceStudioClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.stream.Collectors;

public class MainMenuScreen extends Screen {
    private static final int SIDEBAR_WIDTH = 220;
    private static final int TOOLBAR_HEIGHT = 44;

    private ModernSidebar arenaSidebar;
    private ModernList<String> arenaList;
    private ModernSearchBar searchBar;
    private ModernButton createButton;
    private ModernButton editButton;
    private ModernButton deleteButton;
    private ModernButton runButton;

    private String selectedArenaId;
    private float sidebarAnim;

    public MainMenuScreen() {
        super(Text.literal("Mob Race Studio"));
    }

    @Override
    protected void init() {
        int sw = width;
        int sh = height;

        sidebarAnim = 0f;

        arenaSidebar = new ModernSidebar(0, 0, SIDEBAR_WIDTH, sh, "ARENAS");
        arenaSidebar.setOnSelect(this::onArenaSelected);

        int listX = SIDEBAR_WIDTH + 12;
        int listY = 12;
        int listW = sw - SIDEBAR_WIDTH - 24;
        int listH = sh - TOOLBAR_HEIGHT - 80;

        searchBar = new ModernSearchBar(listX, listY, listW, 32, "Search arenas...");
        searchBar.setOnChange(this::onSearchChanged);

        arenaList = new ModernList<>(listX, listY + 40, listW, listH - 40, 32);
        arenaList.setOnSelect(this::onArenaListSelected);
        arenaList.setOnDoubleClick(idx -> openArenaEditor());

        refreshArenaList();

        int btnY = sh - TOOLBAR_HEIGHT - 8;
        int btnW = 100;
        int btnGap = 8;
        int btnStartX = SIDEBAR_WIDTH + 12;

        createButton = new ModernButton(btnStartX, btnY, btnW, 32, "Create", b -> openCreateDialog());
        editButton = new ModernButton(btnStartX + btnW + btnGap, btnY, btnW, 32, "Edit", b -> openArenaEditor());
        editButton.active = false;
        deleteButton = new ModernButton(btnStartX + (btnW + btnGap) * 2, btnY, btnW, 32, "Delete", b -> confirmDelete());
        deleteButton.active = false;
        runButton = new ModernButton(btnStartX + (btnW + btnGap) * 3, btnY, btnW, 32, "Run Race", b -> openRaceControl());
        runButton.active = false;

        addDrawableChild(createButton);
        addDrawableChild(editButton);
        addDrawableChild(deleteButton);
        addDrawableChild(runButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        sidebarAnim = MathHelper.lerp(delta * 0.1f, sidebarAnim, 1f);

        arenaSidebar.render(ctx, mouseX, mouseY, delta);

        int listX = SIDEBAR_WIDTH + 12;
        int listY = 12;

        GuiRenderHelper.fill(ctx, SIDEBAR_WIDTH, 0, width - SIDEBAR_WIDTH, height, GuiRenderHelper.COLOR_BG_DARK);

        GuiRenderHelper.drawText(ctx, "MOB RACE STUDIO", listX, listY + 4, GuiRenderHelper.COLOR_ACCENT);
        GuiRenderHelper.fill(ctx, listX, listY + 20, 60, 2, GuiRenderHelper.COLOR_ACCENT);

        searchBar.render(ctx, mouseX, mouseY, delta);
        arenaList.render(ctx, mouseX, mouseY, delta);

        super.render(ctx, mouseX, mouseY, delta);

        if (arenaList.getSelectedItem() == null) {
            GuiRenderHelper.drawTextCentered(ctx, "Select an arena or create a new one",
                width / 2 + SIDEBAR_WIDTH / 2, height / 2, GuiRenderHelper.COLOR_TEXT_DISABLED);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        searchBar.mouseClicked(mouseX, mouseY, button);
        arenaList.mouseClicked(mouseX, mouseY, button);
        arenaSidebar.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        if (mouseX > SIDEBAR_WIDTH) {
            arenaList.mouseScrolled(mouseX, mouseY, verticalAmount);
        } else {
            arenaSidebar.mouseScrolled(mouseX, mouseY, verticalAmount);
        }
        return super.mouseScrolled(mouseX, mouseY, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBar.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchBar.charTyped(chr, modifiers)) return true;
        return super.charTyped(chr, modifiers);
    }

    private void refreshArenaList() {
        var arenaNames = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getArenaNames();
        var items = arenaNames.stream()
            .map(name -> new ModernList.ListItem<>(name, name))
            .collect(Collectors.toList());
        arenaList.setItems(items);
    }

    private void onSearchChanged(String query) {
        var allArenas = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getAllArenas();
        var filtered = allArenas.stream()
            .filter(a -> a.getName() != null && a.getName().toLowerCase().contains(query.toLowerCase()))
            .map(a -> new ModernList.ListItem<>(a.getName(), a.getId()))
            .collect(Collectors.toList());
        arenaList.setItems(filtered);
    }

    private void onArenaSelected(int index) {
        if (index >= 0) {
            var arenas = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getAllArenas();
            if (index < arenas.size()) {
                selectedArenaId = arenas.get(index).getId();
                updateButtonStates();
            }
        }
    }

    private void onArenaListSelected(Integer index) {
        if (index != null && index >= 0) {
            var item = arenaList.getSelectedValue();
            if (item != null) {
                selectedArenaId = item;
                updateButtonStates();
            }
        }
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedArenaId != null;
        editButton.active = hasSelection;
        deleteButton.active = hasSelection;
        runButton.active = hasSelection;
    }

    private void openArenaEditor() {
        if (selectedArenaId == null) return;
        var arena = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getArena(selectedArenaId);
        if (arena != null) {
            client.setScreen(new ArenaEditorScreen(arena));
        }
    }

    private void openCreateDialog() {
        client.setScreen(new CreateArenaScreen(this));
    }

    private void confirmDelete() {
        if (selectedArenaId == null) return;
        var arena = MobRaceStudio.getInstance().getStorageManager().getArenaManager().getArena(selectedArenaId);
        if (arena != null) {
            var storage = MobRaceStudio.getInstance().getStorageManager();
            var server = MobRaceStudio.getInstance().getServer();
            if (server != null) {
                storage.deleteArena(selectedArenaId, server);
            }
            selectedArenaId = null;
            refreshArenaList();
            updateButtonStates();
        }
    }

    private void openRaceControl() {
        if (selectedArenaId == null) return;
        client.setScreen(new RaceControlScreen(selectedArenaId));
    }
}
