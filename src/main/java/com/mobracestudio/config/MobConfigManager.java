package com.mobracestudio.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mobracestudio.MobRaceStudio;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class MobConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, MobConfig> configs = new HashMap<>();

    private Path getConfigDir(MinecraftServer server) {
        Path worldPath = server.getRunDirectory().toPath().resolve("saves").resolve(server.getSaveProperties().getLevelName());
        Path configDir = worldPath.resolve("data").resolve("mobracestudio").resolve("mobconfigs");
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Could not create mob config directory", e);
        }
        return configDir;
    }

    public void saveConfig(MobConfig config) {
        configs.put(config.getEntityId(), config);
        MobRaceStudio.LOGGER.debug("Saved mob config for: {}", config.getEntityId());
    }

    public MobConfig getConfig(String entityId) {
        return configs.getOrDefault(entityId, new MobConfig(entityId));
    }

    public void saveAllToDisk(MinecraftServer server) {
        try {
            Path configFile = getConfigDir(server).resolve("mob_configs.json");
            String json = GSON.toJson(configs);
            Files.writeString(configFile, json, StandardCharsets.UTF_8);
            MobRaceStudio.LOGGER.info("Saved {} mob configs", configs.size());
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to save mob configs", e);
        }
    }

    public void loadAllFromDisk(MinecraftServer server) {
        try {
            Path configFile = getConfigDir(server).resolve("mob_configs.json");
            if (Files.exists(configFile)) {
                String json = Files.readString(configFile, StandardCharsets.UTF_8);
                Map<String, MobConfig> loaded = GSON.fromJson(json,
                    new TypeToken<Map<String, MobConfig>>() {}.getType());
                if (loaded != null) {
                    configs.putAll(loaded);
                }
                MobRaceStudio.LOGGER.info("Loaded {} mob configs", configs.size());
            }
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to load mob configs", e);
        }
    }

    public Map<String, MobConfig> getAllConfigs() {
        return configs;
    }

    public void clear() {
        configs.clear();
    }
}
