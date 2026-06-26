package com.mobracestudio.ai;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class ObstacleDetection {
    private final MobEntity mob;
    private static final double RAY_DISTANCE = 3.0;
    private static final double STEP_HEIGHT = 1.0;

    public ObstacleDetection(MobEntity mob) {
        this.mob = mob;
    }

    public boolean isPathBlocked(Vec3d target) {
        Vec3d start = mob.getEyePos();
        Vec3d direction = target.subtract(start).normalize();
        Vec3d end = start.add(direction.multiply(RAY_DISTANCE));

        HitResult result = mob.getWorld().raycast(
            new RaycastContext(start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mob));

        return result.getType() != HitResult.Type.MISS;
    }

    public boolean canJumpOver(Vec3d target) {
        Vec3d start = mob.getPos();
        Vec3d direction = target.subtract(start).normalize();
        Vec3d checkPoint = start.add(direction.multiply(1.5));

        Box checkBox = new Box(checkPoint.add(-0.3, 0, -0.3), checkPoint.add(0.3, STEP_HEIGHT, 0.3));

        return !mob.getWorld().isSpaceEmpty(mob, checkBox);
    }

    public boolean isNearEdge() {
        Vec3d pos = mob.getPos();
        Vec3d lookVec = Vec3d.fromPolar(0, mob.getYaw()).normalize();
        Vec3d checkPos = pos.add(lookVec.multiply(2));

        Box box = new Box(checkPos.add(-0.3, -0.1, -0.3), checkPos.add(0.3, 0.1, 0.3));
        return mob.getWorld().isSpaceEmpty(mob, box);
    }

    public Vec3d findAlternativePath(Vec3d target) {
        Vec3d pos = mob.getPos();
        Vec3d toTarget = target.subtract(pos).normalize();

        Vec3d right = new Vec3d(-toTarget.z, 0, toTarget.x).normalize();
        Vec3d left = new Vec3d(toTarget.z, 0, -toTarget.x).normalize();

        Vec3d[] alternatives = {
            pos.add(right.multiply(2)).add(toTarget),
            pos.add(left.multiply(2)).add(toTarget),
            pos.add(right.multiply(3)).add(toTarget),
            pos.add(left.multiply(3)).add(toTarget)
        };

        for (Vec3d alt : alternatives) {
            if (!isPathBlocked(alt)) {
                return alt;
            }
        }

        return target;
    }
}
