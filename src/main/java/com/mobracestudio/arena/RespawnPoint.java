package com.mobracestudio.arena;

import net.minecraft.util.math.Vec3d;

public class RespawnPoint {
    private Vec3d position;
    private float yaw;
    private int order;

    public RespawnPoint() {}

    public RespawnPoint(Vec3d position, float yaw, int order) {
        this.position = position;
        this.yaw = yaw;
        this.order = order;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
