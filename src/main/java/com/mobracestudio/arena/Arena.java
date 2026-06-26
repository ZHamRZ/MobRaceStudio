package com.mobracestudio.arena;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {
    private String id;
    private String name;
    private Vec3d startPosition;
    private Vec3d finishPosition;
    private final List<Checkpoint> checkpoints = new ArrayList<>();
    private final List<SpawnPoint> spawnPoints = new ArrayList<>();
    private final List<Barrier> barriers = new ArrayList<>();
    private final List<SafeZone> safeZones = new ArrayList<>();
    private final List<RespawnPoint> respawnPoints = new ArrayList<>();
    private int laps = 1;
    private long createdAt;
    private long updatedAt;

    public Arena() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public Arena(String name) {
        this();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public Vec3d getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vec3d pos) {
        this.startPosition = pos;
        this.updatedAt = System.currentTimeMillis();
    }

    public Vec3d getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(Vec3d pos) {
        this.finishPosition = pos;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void addCheckpoint(Checkpoint cp) {
        checkpoints.add(cp);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeCheckpoint(int index) {
        if (index >= 0 && index < checkpoints.size()) {
            checkpoints.remove(index);
            this.updatedAt = System.currentTimeMillis();
        }
    }

    public void clearCheckpoints() {
        checkpoints.clear();
        this.updatedAt = System.currentTimeMillis();
    }

    public List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public void addSpawnPoint(SpawnPoint sp) {
        spawnPoints.add(sp);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeSpawnPoint(int index) {
        if (index >= 0 && index < spawnPoints.size()) {
            spawnPoints.remove(index);
            this.updatedAt = System.currentTimeMillis();
        }
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void addBarrier(Barrier barrier) {
        barriers.add(barrier);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeBarrier(int index) {
        if (index >= 0 && index < barriers.size()) {
            barriers.remove(index);
            this.updatedAt = System.currentTimeMillis();
        }
    }

    public List<SafeZone> getSafeZones() {
        return safeZones;
    }

    public void addSafeZone(SafeZone zone) {
        safeZones.add(zone);
        this.updatedAt = System.currentTimeMillis();
    }

    public List<RespawnPoint> getRespawnPoints() {
        return respawnPoints;
    }

    public void addRespawnPoint(RespawnPoint rp) {
        respawnPoints.add(rp);
        this.updatedAt = System.currentTimeMillis();
    }

    public int getLaps() {
        return laps;
    }

    public void setLaps(int laps) {
        this.laps = Math.max(1, laps);
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean isValid() {
        return name != null && !name.isEmpty()
            && startPosition != null
            && finishPosition != null
            && !spawnPoints.isEmpty();
    }

    public int getTotalCheckpoints() {
        return checkpoints.size();
    }
}
