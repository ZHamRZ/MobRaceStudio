package com.mobracestudio.event;

import com.mobracestudio.MobRaceStudio;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ServerEvents {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            MobRaceStudio.getInstance().getStorageManager().loadAll(server);
            MobRaceStudio.LOGGER.info("Server started - MobRaceStudio data loaded");
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            MobRaceStudio.getInstance().getStorageManager().saveAll(server);
            MobRaceStudio.LOGGER.info("Server stopping - MobRaceStudio data saved");
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            MobRaceStudio.LOGGER.debug("Player joined: {}", handler.player.getName().getString());
        });
    }
}
