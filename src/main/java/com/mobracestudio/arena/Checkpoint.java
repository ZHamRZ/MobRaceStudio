package com.mobracestudio.arena;

import net.minecraft.util.math.Vec3d;

public class Checkpoint {
    private Vec3d position;
    private float radius;
    private int order;

    public Checkpoint() {
        this.radius = 3.0f;
    }

    public Checkpoint(Vec3d position, int order) {
        this.position = position;
        this.radius = 3.0f;
        this.order = order;
    }

    public Vec3d getPosition() {
        return position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = Math.max(1.0f, radius);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
