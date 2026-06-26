package com.mobracestudio.ai;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class AntiStuckGoal {
    private final MobEntity mob;
    private static final int STUCK_THRESHOLD = 60;
    private static final int TELEPORT_AFTER = 200;

    private int stuckTicks;
    private Vec3d lastPos;
    private double lastDistance;
    private boolean recovering;

    public AntiStuckGoal(MobEntity mob) {
        this.mob = mob;
        this.lastPos = mob.getPos();
        this.lastDistance = Double.MAX_VALUE;
    }

    public void tick(Vec3d targetPosition) {
        Vec3d currentPos = mob.getPos();
        double moved = currentPos.distanceTo(lastPos);
        double distanceToTarget = targetPosition != null ? currentPos.distanceTo(targetPosition) : 0;

        if (moved < 0.05) {
            stuckTicks++;
        } else {
            stuckTicks = Math.max(0, stuckTicks - 2);
            lastDistance = distanceToTarget;
        }

        if (distanceToTarget > lastDistance + 1.0) {
            stuckTicks += 5;
        }

        if (stuckTicks > STUCK_THRESHOLD && !recovering) {
            recovering = true;
            performRecovery(targetPosition);
        }

        if (stuckTicks > TELEPORT_AFTER && targetPosition != null) {
            mob.requestTeleport(targetPosition.x, targetPosition.y, targetPosition.z);
            stuckTicks = 0;
            recovering = false;
        }

        if (moved > 0.1) {
            recovering = false;
        }

        lastPos = currentPos;
        lastDistance = distanceToTarget;
    }

    private void performRecovery(Vec3d target) {
        Vec3d pos = mob.getPos();
        Vec3d up = new Vec3d(0, 1, 0);
        mob.setVelocity(up);

        if (target != null) {
            float yaw = (float) Math.toDegrees(Math.atan2(-(target.x - pos.x), target.z - pos.z));
            mob.setYaw(yaw);
        }
    }

    public boolean isStuck() {
        return stuckTicks > STUCK_THRESHOLD;
    }

    public int getStuckTicks() {
        return stuckTicks;
    }

    public void reset() {
        stuckTicks = 0;
        recovering = false;
        lastPos = mob.getPos();
    }
}
