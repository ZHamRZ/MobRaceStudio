package com.mobracestudio.event;

import com.mobracestudio.MobRaceStudio;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class WorldEvents {
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            MobRaceStudio.LOGGER.debug("World loaded: {}", world.getRegistryKey().getValue());
        });

        ServerWorldEvents.UNLOAD.register((server, world) -> {
            MobRaceStudio.LOGGER.debug("World unloaded: {}", world.getRegistryKey().getValue());
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
        });
    }
}
