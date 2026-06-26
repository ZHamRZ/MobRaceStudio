package com.mobracestudio.race;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LapManager {
    private final int totalLaps;
    private final int totalCheckpoints;
    private final Map<UUID, Integer> mobLaps;
    private final Map<UUID, Integer> mobCheckpoints;

    public LapManager(int totalLaps, int totalCheckpoints) {
        this.totalLaps = Math.max(1, totalLaps);
        this.totalCheckpoints = totalCheckpoints;
        this.mobLaps = new HashMap<>();
        this.mobCheckpoints = new HashMap<>();
    }

    public void registerMob(UUID mobId) {
        mobLaps.put(mobId, 0);
        mobCheckpoints.put(mobId, 0);
    }

    public boolean passCheckpoint(UUID mobId, int checkpointIndex) {
        int currentCp = mobCheckpoints.getOrDefault(mobId, 0);

        if (checkpointIndex == currentCp) {
            mobCheckpoints.put(mobId, currentCp + 1);

            if (currentCp + 1 >= totalCheckpoints) {
                int currentLap = mobLaps.getOrDefault(mobId, 0);
                mobLaps.put(mobId, currentLap + 1);
                mobCheckpoints.put(mobId, 0);
                return currentLap + 1 >= totalLaps;
            }
        }
        return false;
    }

    public int getCurrentLap(UUID mobId) {
        return Math.min(mobLaps.getOrDefault(mobId, 0) + 1, totalLaps);
    }

    public int getCurrentCheckpoint(UUID mobId) {
        return mobCheckpoints.getOrDefault(mobId, 0);
    }

    public boolean hasFinished(UUID mobId) {
        return mobLaps.getOrDefault(mobId, 0) >= totalLaps;
    }

    public int getTotalLaps() {
        return totalLaps;
    }

    public int getTotalCheckpoints() {
        return totalCheckpoints;
    }

    public void reset() {
        mobLaps.clear();
        mobCheckpoints.clear();
    }
}
