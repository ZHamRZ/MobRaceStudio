package com.mobracestudio.race;

import java.util.*;
import java.util.stream.Collectors;

public class Leaderboard {
    private final List<LeaderboardEntry> entries;

    public Leaderboard() {
        this.entries = new ArrayList<>();
    }

    public void registerMob(UUID mobId, String mobName) {
        entries.removeIf(e -> e.mobId.equals(mobId));
        entries.add(new LeaderboardEntry(mobId, mobName));
    }

    public void updateProgress(UUID mobId, int lap, int checkpoint, float time) {
        entries.stream()
            .filter(e -> e.mobId.equals(mobId))
            .findFirst()
            .ifPresent(e -> {
                e.lap = lap;
                e.checkpoint = checkpoint;
                e.time = time;
            });
        sort();
    }

    public void finishMob(UUID mobId, float time) {
        entries.stream()
            .filter(e -> e.mobId.equals(mobId))
            .findFirst()
            .ifPresent(e -> {
                e.finished = true;
                e.time = time;
                e.finishTime = System.currentTimeMillis();
            });
        sort();
    }

    private void sort() {
        entries.sort((a, b) -> {
            if (a.finished != b.finished) return a.finished ? -1 : 1;
            if (a.lap != b.lap) return Integer.compare(b.lap, a.lap);
            if (a.checkpoint != b.checkpoint) return Integer.compare(b.checkpoint, a.checkpoint);
            return Float.compare(a.time, b.time);
        });
    }

    public int getPosition(UUID mobId) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).mobId.equals(mobId)) {
                return i + 1;
            }
        }
        return -1;
    }

    public List<LeaderboardEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public void clear() {
        entries.clear();
    }

    public static class LeaderboardEntry {
        public final UUID mobId;
        public final String mobName;
        public int lap;
        public int checkpoint;
        public float time;
        public boolean finished;
        public long finishTime;

        public LeaderboardEntry(UUID mobId, String mobName) {
            this.mobId = mobId;
            this.mobName = mobName;
            this.lap = 1;
            this.checkpoint = 0;
            this.time = 0;
            this.finished = false;
        }

        public String getFormattedTime() {
            return RaceTimer.formatTime(time);
        }
    }
}
