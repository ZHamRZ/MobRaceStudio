package com.mobracestudio.ai;

import com.mobracestudio.arena.Checkpoint;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SmartMovement {
    private final MobEntity mob;
    private final RaceNavigation navigation;
    private final CheckpointGoal checkpointGoal;
    private final ObstacleDetection obstacleDetection;
    private final JumpDetection jumpDetection;
    private final WaterRecoveryGoal waterRecovery;
    private final LadderGoal ladderGoal;
    private final AntiStuckGoal antiStuckGoal;

    private Vec3d currentTarget;
    private boolean enabled;

    public SmartMovement(MobEntity mob, List<Checkpoint> checkpoints) {
        this.mob = mob;
        this.navigation = new RaceNavigation(mob);
        this.checkpointGoal = new CheckpointGoal(mob, navigation, checkpoints);
        this.obstacleDetection = new ObstacleDetection(mob);
        this.jumpDetection = new JumpDetection(mob);
        this.waterRecovery = new WaterRecoveryGoal(mob);
        this.ladderGoal = new LadderGoal(mob);
        this.antiStuckGoal = new AntiStuckGoal(mob);
        this.enabled = true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void tick() {
        if (!enabled) return;

        jumpDetection.tick();
        waterRecovery.tick();
        ladderGoal.tick();

        if (checkpointGoal.hasCompletedAll()) {
            return;
        }

        checkpointGoal.tick();
        currentTarget = navigation.getTargetPosition();

        antiStuckGoal.tick(currentTarget);

        if (obstacleDetection.isPathBlocked(currentTarget)) {
            Vec3d altPath = obstacleDetection.findAlternativePath(currentTarget);
            navigation.setTargetPosition(altPath);
        }

        if (jumpDetection.shouldJump()) {
            jumpDetection.jump();
        }

        if (waterRecovery.needsRecovery()) {
            navigation.resetStuck();
        }

        navigation.tick();
    }

    public CheckpointGoal getCheckpointGoal() {
        return checkpointGoal;
    }

    public RaceNavigation getNavigation() {
        return navigation;
    }

    public boolean hasFinishedCourse() {
        return checkpointGoal.hasCompletedAll();
    }

    public int getCurrentCheckpointIndex() {
        return checkpointGoal.getCurrentCheckpointIndex();
    }

    public void reset() {
        checkpointGoal.reset();
        antiStuckGoal.reset();
    }
}
