package com.mobracestudio.race;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.arena.Arena;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;

public class RaceManager {
    private RaceState state;
    private Arena currentArena;
    private final RaceTimer timer;
    private final CountdownHandler countdown;
    private final Leaderboard leaderboard;
    private LapManager lapManager;
    private ServerWorld world;

    public RaceManager() {
        this.state = RaceState.IDLE;
        this.timer = new RaceTimer();
        this.countdown = new CountdownHandler();
        this.leaderboard = new Leaderboard();
    }

    public boolean startRace(Arena arena, ServerWorld world) {
        if (state != RaceState.IDLE) return false;
        if (!arena.isValid()) {
            MobRaceStudio.LOGGER.warn("Cannot start race: arena '{}' is not valid", arena.getName());
            return false;
        }

        this.currentArena = arena;
        this.world = world;
        this.lapManager = new LapManager(arena.getLaps(), arena.getTotalCheckpoints());

        this.state = RaceState.COUNTDOWN;
        this.countdown.start();

        MobRaceStudio.LOGGER.info("Race started for arena: {}", arena.getName());
        return true;
    }

    public void tick() {
        if (state == RaceState.COUNTDOWN) {
            if (countdown.tick()) {
                state = RaceState.RACING;
                timer.start();
                MobRaceStudio.LOGGER.info("Race is now RACING!");
            }
        }

        if (state == RaceState.RACING) {
            checkAllFinished();
        }
    }

    public boolean passCheckpoint(UUID mobId, int checkpointIndex) {
        if (state != RaceState.RACING) return false;
        if (lapManager == null) return false;

        boolean finished = lapManager.passCheckpoint(mobId, checkpointIndex);

        leaderboard.updateProgress(mobId,
            lapManager.getCurrentLap(mobId),
            lapManager.getCurrentCheckpoint(mobId),
            timer.getElapsedSeconds());

        if (finished) {
            leaderboard.finishMob(mobId, timer.getElapsedSeconds());
            MobRaceStudio.LOGGER.info("Mob {} finished the race!", mobId);
        }

        return finished;
    }

    private void checkAllFinished() {
        if (lapManager != null && leaderboard.getEntries().stream().allMatch(e -> e.finished)) {
            finishRace();
        }
    }

    public void stopRace() {
        if (state == RaceState.RACING || state == RaceState.COUNTDOWN) {
            state = RaceState.STOPPED;
            timer.stop();
            countdown.stop();
            MobRaceStudio.LOGGER.info("Race stopped.");
        }
    }

    public void resetRace() {
        state = RaceState.IDLE;
        currentArena = null;
        timer.stop();
        countdown.stop();
        leaderboard.clear();
        if (lapManager != null) {
            lapManager.reset();
            lapManager = null;
        }
        world = null;
        MobRaceStudio.LOGGER.info("Race reset.");
    }

    private void finishRace() {
        state = RaceState.FINISHED;
        timer.stop();
        MobRaceStudio.LOGGER.info("Race finished! Winner: {}", leaderboard.getEntries().isEmpty() ? "none" : leaderboard.getEntries().get(0).mobName);
    }

    public RaceState getState() {
        return state;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public RaceTimer getTimer() {
        return timer;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public LapManager getLapManager() {
        return lapManager;
    }

    public CountdownHandler getCountdown() {
        return countdown;
    }
}
