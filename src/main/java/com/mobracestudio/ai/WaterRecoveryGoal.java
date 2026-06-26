package com.mobracestudio.ai;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Vec3d;

public class WaterRecoveryGoal {
    private final MobEntity mob;
    private boolean inWater;
    private int swimTimer;

    public WaterRecoveryGoal(MobEntity mob) {
        this.mob = mob;
        this.swimTimer = 0;
    }

    public void tick() {
        inWater = mob.isTouchingWater();

        if (inWater) {
            swimTimer++;

            Vec3d motion = mob.getVelocity();
            if (motion.y < 0) {
                mob.setVelocity(motion.x, Math.min(motion.y + 0.04, 0.3), motion.z);
            }

            if (mob.getY() < mob.getWorld().getSeaLevel() - 5) {
                mob.setVelocity(motion.x, 0.3, motion.z);
            }
        } else {
            swimTimer = 0;
        }
    }

    public boolean isInWater() {
        return inWater;
    }

    public int getSwimTimer() {
        return swimTimer;
    }

    public boolean needsRecovery() {
        return inWater && swimTimer > 20;
    }
}
