package com.mobracestudio.editor;

import com.mobracestudio.arena.Arena;
import com.mobracestudio.arena.Checkpoint;
import com.mobracestudio.arena.SpawnPoint;
import com.mobracestudio.arena.Barrier;
import net.minecraft.util.math.Vec3d;

public class EditorManager {
    private boolean active = false;
    private Arena currentArena;
    private EditorTool currentTool = EditorTool.SELECT;
    private Vec3d cursorPosition;
    private int selectedElementIndex = -1;

    public enum EditorTool {
        SELECT,
        START_POSITION,
        FINISH_POSITION,
        CHECKPOINT,
        SPAWN_POINT,
        BARRIER,
        SAFE_ZONE,
        RESPAWN_POINT,
        DELETE,
        MOVE,
        ROTATE,
        DUPLICATE
    }

    public void activate(Arena arena) {
        this.active = true;
        this.currentArena = arena;
        this.currentTool = EditorTool.SELECT;
        this.selectedElementIndex = -1;
    }

    public void deactivate() {
        this.active = false;
        this.currentArena = null;
        this.currentTool = EditorTool.SELECT;
        this.selectedElementIndex = -1;
    }

    public boolean isActive() {
        return active;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public EditorTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(EditorTool tool) {
        this.currentTool = tool;
        this.selectedElementIndex = -1;
    }

    public void setCursorPosition(Vec3d pos) {
        this.cursorPosition = pos;
    }

    public Vec3d getCursorPosition() {
        return cursorPosition;
    }

    public void onClick(Vec3d hitPos) {
        if (currentArena == null) return;

        switch (currentTool) {
            case START_POSITION -> currentArena.setStartPosition(hitPos);
            case FINISH_POSITION -> currentArena.setFinishPosition(hitPos);
            case CHECKPOINT -> {
                Checkpoint cp = new Checkpoint(hitPos, currentArena.getTotalCheckpoints());
                currentArena.addCheckpoint(cp);
            }
            case SPAWN_POINT -> {
                SpawnPoint sp = new SpawnPoint(hitPos, 0);
                currentArena.addSpawnPoint(sp);
            }
            case BARRIER -> {
                Barrier barrier = new Barrier(hitPos, hitPos.add(1, 0, 0));
                currentArena.addBarrier(barrier);
            }
            case DELETE -> deleteElementAt(hitPos);
            default -> {}
        }
    }

    private void deleteElementAt(Vec3d pos) {
        if (currentArena == null) return;

        for (int i = 0; i < currentArena.getCheckpoints().size(); i++) {
            if (currentArena.getCheckpoints().get(i).getPosition().distanceTo(pos) < 2.0) {
                currentArena.removeCheckpoint(i);
                return;
            }
        }

        for (int i = 0; i < currentArena.getSpawnPoints().size(); i++) {
            if (currentArena.getSpawnPoints().get(i).getPosition().distanceTo(pos) < 2.0) {
                currentArena.removeSpawnPoint(i);
                return;
            }
        }

        if (currentArena.getStartPosition() != null && currentArena.getStartPosition().distanceTo(pos) < 2.0) {
            currentArena.setStartPosition(null);
        }
        if (currentArena.getFinishPosition() != null && currentArena.getFinishPosition().distanceTo(pos) < 2.0) {
            currentArena.setFinishPosition(null);
        }
    }

    public int getSelectedElementIndex() {
        return selectedElementIndex;
    }

    public void setSelectedElementIndex(int index) {
        this.selectedElementIndex = index;
    }
}
