package com.mobracestudio.config;

public class RaceConfig {
    private int countdownDuration;
    private boolean allowReplay;
    private boolean enableHUD;
    private boolean broadcastMessages;
    private int maxMobsPerRace;

    public RaceConfig() {
        this.countdownDuration = 3;
        this.allowReplay = true;
        this.enableHUD = true;
        this.broadcastMessages = true;
        this.maxMobsPerRace = 20;
    }

    public int getCountdownDuration() {
        return countdownDuration;
    }

    public void setCountdownDuration(int seconds) {
        this.countdownDuration = Math.max(1, Math.min(10, seconds));
    }

    public boolean isAllowReplay() {
        return allowReplay;
    }

    public void setAllowReplay(boolean allowReplay) {
        this.allowReplay = allowReplay;
    }

    public boolean isEnableHUD() {
        return enableHUD;
    }

    public void setEnableHUD(boolean enableHUD) {
        this.enableHUD = enableHUD;
    }

    public boolean isBroadcastMessages() {
        return broadcastMessages;
    }

    public void setBroadcastMessages(boolean broadcastMessages) {
        this.broadcastMessages = broadcastMessages;
    }

    public int getMaxMobsPerRace() {
        return maxMobsPerRace;
    }

    public void setMaxMobsPerRace(int max) {
        this.maxMobsPerRace = Math.max(1, max);
    }
}
