package com.mobracestudio.race;

public class RaceTimer {
    private long startTime;
    private long pausedTime;
    private long totalPausedDuration;
    private boolean running;
    private boolean paused;

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.totalPausedDuration = 0;
        this.running = true;
        this.paused = false;
    }

    public void stop() {
        this.running = false;
        this.paused = false;
    }

    public void pause() {
        if (running && !paused) {
            this.pausedTime = System.currentTimeMillis();
            this.paused = true;
        }
    }

    public void resume() {
        if (running && paused) {
            this.totalPausedDuration += System.currentTimeMillis() - pausedTime;
            this.paused = false;
        }
    }

    public float getElapsedSeconds() {
        if (!running) return 0;
        long now = System.currentTimeMillis();
        long elapsed = now - startTime - totalPausedDuration - (paused ? now - pausedTime : 0);
        return elapsed / 1000.0f;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public String getFormattedTime() {
        return formatTime(getElapsedSeconds());
    }

    public static String formatTime(float seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        int millis = (int) ((seconds - (int) seconds) * 100);
        return String.format("%02d:%02d.%02d", mins, secs, millis);
    }
}
