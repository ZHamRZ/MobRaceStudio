package com.mobracestudio.arena;

import net.minecraft.util.math.Vec3d;

public class SafeZone {
    private Vec3d center;
    private float radius;

    public SafeZone() {
        this.radius = 5.0f;
    }

    public SafeZone(Vec3d center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Vec3d getCenter() {
        return center;
    }

    public void setCenter(Vec3d center) {
        this.center = center;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = Math.max(1.0f, radius);
    }
}
