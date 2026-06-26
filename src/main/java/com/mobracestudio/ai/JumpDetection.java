package com.mobracestudio.ai;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class JumpDetection {
    private final MobEntity mob;
    private static final double JUMP_THRESHOLD = 0.5;
    private int jumpCooldown;

    public JumpDetection(MobEntity mob) {
        this.mob = mob;
        this.jumpCooldown = 0;
    }

    public void tick() {
        if (jumpCooldown > 0) jumpCooldown--;
    }

    public boolean shouldJump() {
        if (jumpCooldown > 0) return false;
        if (!mob.isOnGround()) return false;

        Vec3d pos = mob.getPos();
        Vec3d lookVec = Vec3d.fromPolar(0, mob.getYaw()).normalize();
        Vec3d ahead = pos.add(lookVec.multiply(1.0));

        Box checkBox = new Box(ahead.add(-0.3, 0, -0.3), ahead.add(0.3, JUMP_THRESHOLD, 0.3));
        boolean blocked = !mob.getWorld().isSpaceEmpty(mob, checkBox);

        if (blocked) {
            jumpCooldown = 10;
            return true;
        }

        return false;
    }

    public void jump() {
        mob.setVelocity(mob.getVelocity().add(0, 0.42, 0));
        mob.velocityDirty = true;
        jumpCooldown = 10;
    }

    public int getJumpCooldown() {
        return jumpCooldown;
    }
}
