package com.mobracestudio.storage;

import com.mobracestudio.MobRaceStudio;
import com.mobracestudio.arena.ArenaManager;
import com.mobracestudio.arena.ArenaStorage;
import com.mobracestudio.arena.Arena;
import com.mobracestudio.config.MobConfigManager;
import net.minecraft.server.MinecraftServer;

public class StorageManager {
    private final ArenaStorage arenaStorage;
    private final ArenaManager arenaManager;
    private final MobConfigManager mobConfigManager;

    public StorageManager() {
        this.arenaStorage = new ArenaStorage();
        this.arenaManager = new ArenaManager();
        this.mobConfigManager = new MobConfigManager();
    }

    public ArenaStorage getArenaStorage() {
        return arenaStorage;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public MobConfigManager getMobConfigManager() {
        return mobConfigManager;
    }

    public void loadAll(MinecraftServer server) {
        MobRaceStudio.LOGGER.info("Loading Mob Race Studio data...");
        var arenas = arenaStorage.loadAllArenas(server);
        arenaManager.clear();
        arenaManager.importArenas(arenas);
        mobConfigManager.loadAllFromDisk(server);
        MobRaceStudio.LOGGER.info("Loaded {} arenas and {} mob configs", arenas.size(), mobConfigManager.getAllConfigs().size());
    }

    public void saveAll(MinecraftServer server) {
        MobRaceStudio.LOGGER.info("Saving Mob Race Studio data...");
        arenaStorage.saveAllArenas(arenaManager.getAllArenas(), server);
        mobConfigManager.saveAllToDisk(server);
        MobRaceStudio.LOGGER.info("Saved {} arenas and {} mob configs",
            arenaManager.getArenaCount(), mobConfigManager.getAllConfigs().size());
    }

    public void saveArena(Arena arena, MinecraftServer server) {
        arenaStorage.saveArena(arena, server);
        arenaManager.addArena(arena);
    }

    public void deleteArena(String id, MinecraftServer server) {
        arenaStorage.deleteArena(id, server);
        arenaManager.removeArena(id);
    }

    public void loadArena(String id, MinecraftServer server) {
        Arena arena = arenaStorage.loadArena(id, server);
        if (arena != null) {
            arenaManager.addArena(arena);
        }
    }
}
