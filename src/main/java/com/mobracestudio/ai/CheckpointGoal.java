package com.mobracestudio.ai;

import com.mobracestudio.arena.Checkpoint;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class CheckpointGoal {
    private final MobEntity mob;
    private final RaceNavigation navigation;
    private final List<Checkpoint> checkpoints;
    private int currentTargetIndex;

    public CheckpointGoal(MobEntity mob, RaceNavigation navigation, List<Checkpoint> checkpoints) {
        this.mob = mob;
        this.navigation = navigation;
        this.checkpoints = checkpoints;
        this.currentTargetIndex = 0;
    }

    public void tick() {
        if (checkpoints.isEmpty()) return;
        if (currentTargetIndex >= checkpoints.size()) return;

        Checkpoint target = checkpoints.get(currentTargetIndex);
        Vec3d targetPos = target.getPosition();

        navigation.setTargetPosition(targetPos);

        if (navigation.hasReachedTarget()) {
            currentTargetIndex++;
        }
    }

    public boolean hasCompletedAll() {
        return currentTargetIndex >= checkpoints.size();
    }

    public int getCurrentCheckpointIndex() {
        return currentTargetIndex;
    }

    public Checkpoint getCurrentCheckpoint() {
        if (currentTargetIndex < checkpoints.size()) {
            return checkpoints.get(currentTargetIndex);
        }
        return null;
    }

    public void setCurrentCheckpointIndex(int index) {
        this.currentTargetIndex = Math.max(0, Math.min(index, checkpoints.size()));
    }

    public void reset() {
        this.currentTargetIndex = 0;
    }
}
