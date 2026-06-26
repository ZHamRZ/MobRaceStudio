package com.mobracestudio.gui.screen;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.config.MobConfig;
import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.*;
import com.mobracestudio.registry.EntityRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MobConfigScreen extends Screen {
    private static final int SIDEBAR_WIDTH = 240;

    private final List<MobConfigItem> mobItems = new ArrayList<>();
    private ModernSearchBar searchBar;
    private ModernList<EntityType<?>> mobList;
    private ModernPropertyPanel configPanel;
    private ModernButton saveButton;
    private ModernButton backButton;
    private ModernCheckbox enableCheckbox;
    private ModernSlider speedSlider;
    private ModernSlider jumpSlider;
    private ModernSlider healthSlider;
    private String filterQuery = "";
    private EntityType<?> selectedMob;

    public MobConfigScreen() {
        super(Text.literal("Mob Configuration"));
    }

    @Override
    protected void init() {
        int sw = width;
        int sh = height;

        EntityRegistry.discoverAllEntities();
        mobItems.clear();
        EntityRegistry.getAllEntities().forEach(entityType -> {
            Identifier id = EntityRegistry.getEntityTypeId(entityType);
            if (id != null) {
                mobItems.add(new MobConfigItem(entityType, id.toString()));
            }
        });

        searchBar = new ModernSearchBar(12, 12, SIDEBAR_WIDTH - 24, 28, "Search mobs...");
        searchBar.setOnChange(this::onSearchChanged);

        mobList = new ModernList<>(12, 48, SIDEBAR_WIDTH - 24, sh - 60, 24);
        mobList.setOnSelect(this::onMobSelected);
        refreshMobList();

        int panelX = SIDEBAR_WIDTH + 12;
        int panelW = sw - panelX - 12;

        configPanel = new ModernPropertyPanel(panelX, 12, panelW, sh - 60);

        enableCheckbox = new ModernCheckbox(panelX + 12, 20, 16, "Enable in race", false);

        healthSlider = new ModernSlider(panelX + 12, 50, panelW - 24, 20, "Health", 1, 100, 20);
        speedSlider = new ModernSlider(panelX + 12, 100, panelW - 24, 20, "Speed Multiplier", 0.1f, 5.0f, 1.0f);
        jumpSlider = new ModernSlider(panelX + 12, 150, panelW - 24, 20, "Jump Multiplier", 0.1f, 5.0f, 1.0f);

        saveButton = new ModernButton(panelX, sh - 36, 100, 28, "Save Config", b -> saveConfig());
        backButton = new ModernButton(panelX + 108, sh - 36, 100, 28, "Back", b -> close());

        addDrawableChild(saveButton);
        addDrawableChild(backButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        GuiRenderHelper.fill(ctx, 0, 0, SIDEBAR_WIDTH, height, GuiRenderHelper.COLOR_BG_DARK);
        GuiRenderHelper.fill(ctx, SIDEBAR_WIDTH, 0, 1, height, GuiRenderHelper.COLOR_BORDER);
        GuiRenderHelper.fill(ctx, SIDEBAR_WIDTH, 0, width - SIDEBAR_WIDTH, height, GuiRenderHelper.COLOR_BG_MEDIUM);

        searchBar.render(ctx, mouseX, mouseY, delta);
        mobList.render(ctx, mouseX, mouseY, delta);

        if (selectedMob != null) {
            int panelX = SIDEBAR_WIDTH + 12;
            Identifier id = EntityRegistry.getEntityTypeId(selectedMob);

            GuiRenderHelper.drawText(ctx, "Mob Configuration", panelX + 12, 8, GuiRenderHelper.COLOR_ACCENT);
            GuiRenderHelper.drawText(ctx, "Entity: " + (id != null ? id.toString() : "unknown"), panelX + 12, 22, GuiRenderHelper.COLOR_TEXT_SECONDARY);

            enableCheckbox.render(ctx, mouseX, mouseY, delta);
            healthSlider.render(ctx, mouseX, mouseY, delta);
            speedSlider.render(ctx, mouseX, mouseY, delta);
            jumpSlider.render(ctx, mouseX, mouseY, delta);
        } else {
            GuiRenderHelper.drawTextCentered(ctx, "Select a mob to configure",
                width / 2 + SIDEBAR_WIDTH / 2, height / 2, GuiRenderHelper.COLOR_TEXT_DISABLED);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        searchBar.mouseClicked(mouseX, mouseY, button);
        mobList.mouseClicked(mouseX, mouseY, button);
        enableCheckbox.mouseClicked(mouseX, mouseY, button);
        healthSlider.mouseClicked(mouseX, mouseY, button);
        speedSlider.mouseClicked(mouseX, mouseY, button);
        jumpSlider.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        healthSlider.mouseReleased(mouseX, mouseY, button);
        speedSlider.mouseReleased(mouseX, mouseY, button);
        jumpSlider.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        healthSlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        speedSlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        jumpSlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        mobList.mouseScrolled(mouseX, mouseY, verticalAmount);
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

    @SuppressWarnings("unchecked")
    private void refreshMobList() {
        String query = filterQuery.toLowerCase();
        var filtered = mobItems.stream()
            .filter(item -> query.isEmpty() || item.displayName.toLowerCase().contains(query))
            .map(item -> (ModernList.ListItem<EntityType<?>>) new ModernList.ListItem<>(item.displayName, item.entityType))
            .collect(Collectors.toList());
        mobList.setItems(filtered);
    }

    private void onSearchChanged(String query) {
        this.filterQuery = query;
        refreshMobList();
    }

    private void onMobSelected(Integer index) {
        if (index != null && index >= 0) {
            this.selectedMob = mobList.getSelectedValue();
        }
    }

    private void saveConfig() {
        if (selectedMob == null) return;
        Identifier id = EntityRegistry.getEntityTypeId(selectedMob);
        if (id == null) return;

        MobConfig config = new MobConfig(id.toString());
        config.setEnabled(enableCheckbox.isChecked());
        config.setHealth(healthSlider.getValue());
        config.setSpeedMultiplier(speedSlider.getValue());
        config.setJumpMultiplier(jumpSlider.getValue());

        MobRaceStudio.getInstance().getStorageManager().getMobConfigManager().saveConfig(config);
        MobRaceStudio.LOGGER.info("Saved config for mob: {}", id);
    }

    private static class MobConfigItem {
        final EntityType<?> entityType;
        final String displayName;

        MobConfigItem(EntityType<?> entityType, String id) {
            this.entityType = entityType;
            String name = entityType.getName().getString();
            this.displayName = name != null && !name.isEmpty() ? name : id;
        }
    }
}
