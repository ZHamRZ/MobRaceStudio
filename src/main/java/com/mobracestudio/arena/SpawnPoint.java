package com.mobracestudio.arena;

import net.minecraft.util.math.Vec3d;

public class SpawnPoint {
    private Vec3d position;
    private float yaw;

    public SpawnPoint() {}

    public SpawnPoint(Vec3d position, float yaw) {
        this.position = position;
        this.yaw = yaw;
    }

    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
