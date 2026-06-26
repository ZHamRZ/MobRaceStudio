package com.mobracestudio.race;

import java.util.*;

public class RankingSystem {
    private final Map<UUID, Float> finishTimes;
    private final Map<UUID, Integer> mobPositions;

    public RankingSystem() {
        this.finishTimes = new LinkedHashMap<>();
        this.mobPositions = new HashMap<>();
    }

    public void registerMob(UUID mobId) {
        finishTimes.putIfAbsent(mobId, Float.MAX_VALUE);
    }

    public void recordFinish(UUID mobId, float time) {
        finishTimes.put(mobId, Math.min(finishTimes.getOrDefault(mobId, Float.MAX_VALUE), time));
        recalculatePositions();
    }

    private void recalculatePositions() {
        List<Map.Entry<UUID, Float>> sorted = new ArrayList<>(finishTimes.entrySet());
        sorted.sort(Map.Entry.comparingByValue());

        mobPositions.clear();
        for (int i = 0; i < sorted.size(); i++) {
            mobPositions.put(sorted.get(i).getKey(), i + 1);
        }
    }

    public int getPosition(UUID mobId) {
        return mobPositions.getOrDefault(mobId, -1);
    }

    public float getFinishTime(UUID mobId) {
        return finishTimes.getOrDefault(mobId, -1f);
    }

    public List<UUID> getRanking() {
        List<Map.Entry<UUID, Float>> sorted = new ArrayList<>(finishTimes.entrySet());
        sorted.sort(Map.Entry.comparingByValue());
        return sorted.stream().map(Map.Entry::getKey).toList();
    }

    public boolean hasFinished(UUID mobId) {
        return finishTimes.containsKey(mobId) && finishTimes.get(mobId) < Float.MAX_VALUE;
    }

    public int getFinishedCount() {
        return (int) finishTimes.values().stream().filter(t -> t < Float.MAX_VALUE).count();
    }

    public void clear() {
        finishTimes.clear();
        mobPositions.clear();
    }
}
