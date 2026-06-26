package com.mobracestudio.util;

import net.minecraft.util.math.Vec3d;

public class MathHelper {
    public static double distance2D(Vec3d a, Vec3d b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static float angleBetween(Vec3d from, Vec3d to) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        return (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    public static Vec3d lerp(Vec3d a, Vec3d b, float t) {
        return new Vec3d(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        );
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean isInRange(Vec3d pos, Vec3d center, double radius) {
        return pos.distanceTo(center) <= radius;
    }
}
