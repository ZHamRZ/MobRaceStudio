package com.mobracestudio.gui.screen;

import com.mobracestudio.gui.renderer.GuiRenderHelper;
import com.mobracestudio.gui.widget.ModernButton;
import com.mobracestudio.gui.widget.ModernList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreen extends Screen {
    private final Screen parent;
    private final List<LeaderboardEntry> entries;
    private ModernList<LeaderboardEntry> leaderboardList;
    private ModernButton backButton;
    private ModernButton clearButton;

    public LeaderboardScreen(Screen parent, List<LeaderboardEntry> entries) {
        super(Text.literal("Leaderboard"));
        this.parent = parent;
        this.entries = entries != null ? entries : new ArrayList<>();
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int cy = height / 2;
        int listW = Math.min(500, width - 40);
        int listH = height - 100;

        int listX = (width - listW) / 2;

        leaderboardList = new ModernList<>(listX, 40, listW, listH, 28);

        var items = new ArrayList<ModernList.ListItem<LeaderboardEntry>>();
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            String label = (i + 1) + ". " + entry.mobName + " - " + entry.formattedTime;
            items.add(new ModernList.ListItem<>(label, entry));
        }
        leaderboardList.setItems(items);

        backButton = new ModernButton(cx - 110, height - 40, 100, 28, "Back", b -> close());
        clearButton = new ModernButton(cx + 10, height - 40, 100, 28, "Clear", b -> clearLeaderboard());

        addDrawableChild(backButton);
        addDrawableChild(clearButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);

        GuiRenderHelper.fill(ctx, 0, 0, width, height, GuiRenderHelper.COLOR_BG_DARK);
        GuiRenderHelper.drawTextCentered(ctx, "LEADERBOARD", width / 2, 16, GuiRenderHelper.COLOR_ACCENT);

        leaderboardList.render(ctx, mouseX, mouseY, delta);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        leaderboardList.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        leaderboardList.mouseScrolled(mouseX, mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, verticalAmount);
    }

    private void clearLeaderboard() {
        entries.clear();
        leaderboardList.setItems(new ArrayList<>());
    }

    public static class LeaderboardEntry {
        public final String mobName;
        public final String formattedTime;
        public final int position;

        public LeaderboardEntry(String mobName, String formattedTime, int position) {
            this.mobName = mobName;
            this.formattedTime = formattedTime;
            this.position = position;
        }
    }
}
