package com.mobracestudio.race;

import com.mobracestudio.MobRaceStudio;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CountdownHandler {
    private static final int COUNTDOWN_DURATION = 3;
    private int countdown;
    private boolean active;
    private long lastTick;

    public void start() {
        this.countdown = COUNTDOWN_DURATION;
        this.active = true;
        this.lastTick = System.currentTimeMillis();
    }

    public void stop() {
        this.active = false;
        this.countdown = 0;
    }

    public boolean isActive() {
        return active;
    }

    public int getCurrentCount() {
        return countdown;
    }

    public boolean tick() {
        if (!active) return false;

        long now = System.currentTimeMillis();
        if (now - lastTick >= 1000) {
            lastTick = now;

            if (countdown > 0) {
                broadcastCountdown();
                countdown--;
            }

            if (countdown <= 0) {
                broadcastGo();
                active = false;
                return true;
            }
        }
        return false;
    }

    private void broadcastCountdown() {
        if (MobRaceStudio.getInstance().getServer() != null) {
            String text = countdown > 0 ? String.valueOf(countdown) : "GO!";
            MobRaceStudio.getInstance().getServer().getPlayerManager()
                .broadcast(Text.literal("\u00a7e\u00a7l" + text), false);
        }
    }

    private void broadcastGo() {
        if (MobRaceStudio.getInstance().getServer() != null) {
            MobRaceStudio.getInstance().getServer().getPlayerManager()
                .broadcast(Text.literal("\u00a7a\u00a7lGO!"), false);
        }
    }
}
