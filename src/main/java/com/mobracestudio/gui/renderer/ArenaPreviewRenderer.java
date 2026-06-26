package com.mobracestudio.gui.renderer;

import com.mobracestudio.arena.Arena;
import com.mobracestudio.arena.Checkpoint;
import com.mobracestudio.arena.SpawnPoint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.Vec3d;

public class ArenaPreviewRenderer {
    private static final int START_COLOR = 0xFF4CAF50;
    private static final int FINISH_COLOR = 0xFFF44336;
    private static final int CHECKPOINT_COLOR = 0xFF2196F3;
    private static final int SPAWN_COLOR = 0xFFFFC107;
    private static final int BARRIER_COLOR = 0xFF9C27B0;

    public void render(DrawContext ctx, Arena arena, int x, int y, int w, int h, float scale, Vec3d cameraPos) {
        if (arena == null) return;

        GuiRenderHelper.fill(ctx, x, y, w, h, GuiRenderHelper.COLOR_BG_MEDIUM);
        GuiRenderHelper.border(ctx, x, y, w, h, 1, GuiRenderHelper.COLOR_BORDER);

        double minX = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

        if (arena.getStartPosition() != null) {
            minX = Math.min(minX, arena.getStartPosition().x);
            maxX = Math.max(maxX, arena.getStartPosition().x);
            minZ = Math.min(minZ, arena.getStartPosition().z);
            maxZ = Math.max(maxZ, arena.getStartPosition().z);
        }
        if (arena.getFinishPosition() != null) {
            minX = Math.min(minX, arena.getFinishPosition().x);
            maxX = Math.max(maxX, arena.getFinishPosition().x);
            minZ = Math.min(minZ, arena.getFinishPosition().z);
            maxZ = Math.max(maxZ, arena.getFinishPosition().z);
        }
        for (Checkpoint cp : arena.getCheckpoints()) {
            Vec3d p = cp.getPosition();
            if (p != null) {
                minX = Math.min(minX, p.x);
                maxX = Math.max(maxX, p.x);
                minZ = Math.min(minZ, p.z);
                maxZ = Math.max(maxZ, p.z);
            }
        }

        double rangeX = maxX - minX;
        double rangeZ = maxZ - minZ;
        if (rangeX < 1) rangeX = 1;
        if (rangeZ < 1) rangeZ = 1;

        double padX = rangeX * 0.2;
        double padZ = rangeZ * 0.2;
        minX -= padX;
        maxX += padX;
        minZ -= padZ;
        maxZ += padZ;
        rangeX = maxX - minX;
        rangeZ = maxZ - minZ;

        int margin = 20;
        int drawW = w - margin * 2;
        int drawH = h - margin * 2;
        double scaleX = drawW / rangeX;
        double scaleZ = drawH / rangeZ;
        double finalScale = Math.min(scaleX, scaleZ);

        int originX = x + margin + (int) ((drawW - rangeX * finalScale) / 2);
        int originY = y + margin + (int) ((drawH - rangeZ * finalScale) / 2);

        renderPoint(ctx, arena.getStartPosition(), minX, minZ, finalScale, originX, originY, START_COLOR, "S");
        renderPoint(ctx, arena.getFinishPosition(), minX, minZ, finalScale, originX, originY, FINISH_COLOR, "F");

        for (int i = 0; i < arena.getCheckpoints().size(); i++) {
            Checkpoint cp = arena.getCheckpoints().get(i);
            renderPoint(ctx, cp.getPosition(), minX, minZ, finalScale, originX, originY, CHECKPOINT_COLOR, String.valueOf(i + 1));
        }

        for (int i = 0; i < arena.getSpawnPoints().size(); i++) {
            SpawnPoint sp = arena.getSpawnPoints().get(i);
            renderPoint(ctx, sp.getPosition(), minX, minZ, finalScale, originX, originY, SPAWN_COLOR, "M" + (i + 1));
        }
    }

    private void renderPoint(DrawContext ctx, Vec3d pos, double minX, double minZ, double scale, int ox, int oy, int color, String label) {
        if (pos == null) return;
        int px = ox + (int) ((pos.x - minX) * scale);
        int py = oy + (int) ((pos.z - minZ) * scale);

        ctx.fill(px - 3, py - 3, 6, 6, color);
        GuiRenderHelper.drawText(ctx, label, px + 5, py - 4, color);
    }
}
