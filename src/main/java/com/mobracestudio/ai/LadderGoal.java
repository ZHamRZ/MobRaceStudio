package com.mobracestudio.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LadderGoal {
    private final MobEntity mob;
    private BlockPos ladderPos;

    public LadderGoal(MobEntity mob) {
        this.mob = mob;
    }

    public void tick() {
        BlockPos mobPos = mob.getBlockPos();
        BlockState state = mob.getWorld().getBlockState(mobPos);

        if (state.getBlock() instanceof LadderBlock || state.getBlock() instanceof VineBlock) {
            ladderPos = mobPos;
            mob.setVelocity(mob.getVelocity().x, 0.2, mob.getVelocity().z);
        } else {
            ladderPos = null;
        }
    }

    public boolean isOnLadder() {
        return ladderPos != null;
    }

    public boolean isNearLadder() {
        BlockPos mobPos = mob.getBlockPos();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos checkPos = mobPos.add(dx, 0, dz);
                BlockState state = mob.getWorld().getBlockState(checkPos);
                if (state.getBlock() instanceof LadderBlock || state.getBlock() instanceof VineBlock) {
                    return true;
                }
            }
        }
        return false;
    }
}
