package com.mobracestudio.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RaceNavigation {
    private final MobEntity mob;
    private Vec3d targetPosition;
    private float movementSpeed;
    private boolean stuck;
    private int stuckTimer;
    private Vec3d lastPosition;
    private static final int STUCK_THRESHOLD = 40;

    public RaceNavigation(MobEntity mob) {
        this.mob = mob;
        this.movementSpeed = 1.0f;
        this.lastPosition = mob.getPos();
    }

    public void setTargetPosition(Vec3d pos) {
        this.targetPosition = pos;
        this.stuck = false;
        this.stuckTimer = 0;
    }

    public void setMovementSpeed(float speed) {
        this.movementSpeed = speed;
    }

    public void tick() {
        if (targetPosition == null) return;

        Vec3d currentPos = mob.getPos();
        double distance = currentPos.distanceTo(targetPosition);

        Vec3d motion = currentPos.subtract(lastPosition);
        double movedDistance = motion.length();

        if (movedDistance < 0.01) {
            stuckTimer++;
            if (stuckTimer > STUCK_THRESHOLD) {
                stuck = true;
            }
        } else {
            stuckTimer = 0;
            stuck = false;
        }
        lastPosition = currentPos;

        if (distance > 0.5) {
            Vec3d direction = targetPosition.subtract(currentPos).normalize();
            mob.setVelocity(direction.x * movementSpeed * 0.1f, mob.getVelocity().y, direction.z * movementSpeed * 0.1f);

            float yaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
            mob.setYaw(yaw);
            mob.headYaw = yaw;
            mob.bodyYaw = yaw;
        }

        mob.setMovementSpeed(movementSpeed);
    }

    public boolean hasReachedTarget() {
        if (targetPosition == null) return false;
        return mob.getPos().distanceTo(targetPosition) < 2.0;
    }

    public boolean isStuck() {
        return stuck;
    }

    public Vec3d getTargetPosition() {
        return targetPosition;
    }

    public void resetStuck() {
        stuck = false;
        stuckTimer = 0;
    }
}
