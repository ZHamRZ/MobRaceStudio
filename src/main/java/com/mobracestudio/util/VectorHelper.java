package com.mobracestudio.util;

import net.minecraft.util.math.Vec3d;

public class VectorHelper {
    public static Vec3d fromBlockPos(int x, int y, int z) {
        return new Vec3d(x + 0.5, y, z + 0.5);
    }

    public static Vec3d center(Vec3d pos) {
        return new Vec3d(Math.floor(pos.x) + 0.5, pos.y, Math.floor(pos.z) + 0.5);
    }

    public static double horizontalDistance(Vec3d a, Vec3d b) {
        double dx = a.x - b.x;
        double dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    public static Vec3d directionTo(Vec3d from, Vec3d to) {
        return to.subtract(from).normalize();
    }

    public static float yawFromDirection(Vec3d direction) {
        return (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
    }

    public static Vec3d lerp(Vec3d a, Vec3d b, double t) {
        return new Vec3d(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t,
            a.z + (b.z - a.z) * t
        );
    }
}
