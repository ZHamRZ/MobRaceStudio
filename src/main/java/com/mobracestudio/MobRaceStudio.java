package com.mobracestudio;

import com.mobracestudio.event.ServerEvents;
import com.mobracestudio.event.WorldEvents;
import com.mobracestudio.network.NetworkHandler;
import com.mobracestudio.registry.AttributeRegistry;
import com.mobracestudio.registry.EntityRegistry;
import com.mobracestudio.registry.ModRegistry;
import com.mobracestudio.storage.StorageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobRaceStudio implements ModInitializer {
    public static final String MOD_ID = "mobracestudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static MobRaceStudio INSTANCE;
    private StorageManager storageManager;
    private MinecraftServer server;

    public static MobRaceStudio getInstance() {
        return INSTANCE;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public MinecraftServer getServer() {
        return server;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        LOGGER.info("Initializing Mob Race Studio...");

        this.storageManager = new StorageManager();

        ModRegistry.initialize();
        AttributeRegistry.initialize();
        EntityRegistry.discoverAllEntities();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;
        });

        ServerEvents.register();
        WorldEvents.register();
        NetworkHandler.registerServer();

        LOGGER.info("Mob Race Studio initialized successfully!");
        LOGGER.info("Discovered {} entity types across {} mods",
            EntityRegistry.getTotalEntityCount(), EntityRegistry.getModCount());
    }
}
