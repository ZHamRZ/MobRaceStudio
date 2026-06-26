package com.mobracestudio.arena;

import net.minecraft.util.math.Vec3d;

public class Barrier {
    private Vec3d start;
    private Vec3d end;
    private float height;
    private boolean visible;

    public Barrier() {
        this.height = 3.0f;
        this.visible = true;
    }

    public Barrier(Vec3d start, Vec3d end) {
        this();
        this.start = start;
        this.end = end;
    }

    public Vec3d getStart() {
        return start;
    }

    public void setStart(Vec3d start) {
        this.start = start;
    }

    public Vec3d getEnd() {
        return end;
    }

    public void setEnd(Vec3d end) {
        this.end = end;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = Math.max(0.5f, height);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
