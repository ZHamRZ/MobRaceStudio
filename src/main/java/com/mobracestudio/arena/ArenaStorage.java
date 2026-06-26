package com.mobracestudio.arena;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mobracestudio.MobRaceStudio;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ArenaStorage {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Vec3d.class, new Vec3dSerializer())
        .registerTypeAdapter(Vec3d.class, new Vec3dDeserializer())
        .create();

    private Path getStorageDir(MinecraftServer server) {
        Path worldPath = server.getRunDirectory().toPath().resolve("saves").resolve(server.getSaveProperties().getLevelName());
        Path dataDir = worldPath.resolve("data").resolve("mobracestudio").resolve("arenas");
        try {
            Files.createDirectories(dataDir);
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Could not create arena storage directory", e);
        }
        return dataDir;
    }

    public void saveArena(Arena arena, MinecraftServer server) {
        try {
            Path dir = getStorageDir(server);
            Path file = dir.resolve(arena.getId() + ".json");
            String json = GSON.toJson(arena);
            Files.writeString(file, json, StandardCharsets.UTF_8);
            MobRaceStudio.LOGGER.debug("Saved arena: {}", arena.getName());
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to save arena: " + arena.getName(), e);
        }
    }

    public Arena loadArena(String arenaId, MinecraftServer server) {
        try {
            Path dir = getStorageDir(server);
            Path file = dir.resolve(arenaId + ".json");
            if (Files.exists(file)) {
                String json = Files.readString(file, StandardCharsets.UTF_8);
                Arena arena = GSON.fromJson(json, Arena.class);
                MobRaceStudio.LOGGER.debug("Loaded arena: {}", arena != null ? arena.getName() : null);
                return arena;
            }
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to load arena: " + arenaId, e);
        }
        return null;
    }

    public List<Arena> loadAllArenas(MinecraftServer server) {
        List<Arena> arenas = new ArrayList<>();
        try {
            Path dir = getStorageDir(server);
            if (Files.exists(dir)) {
                try (var stream = Files.list(dir)) {
                    stream.filter(p -> p.toString().endsWith(".json")).forEach(file -> {
                        try {
                            String json = Files.readString(file, StandardCharsets.UTF_8);
                            Arena arena = GSON.fromJson(json, Arena.class);
                            if (arena != null) {
                                arenas.add(arena);
                            }
                        } catch (IOException e) {
                            MobRaceStudio.LOGGER.error("Failed to load arena file: " + file, e);
                        }
                    });
                }
            }
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to load all arenas", e);
        }
        return arenas;
    }

    public void deleteArena(String arenaId, MinecraftServer server) {
        try {
            Path dir = getStorageDir(server);
            Path file = dir.resolve(arenaId + ".json");
            Files.deleteIfExists(file);
            MobRaceStudio.LOGGER.debug("Deleted arena: {}", arenaId);
        } catch (IOException e) {
            MobRaceStudio.LOGGER.error("Failed to delete arena: " + arenaId, e);
        }
    }

    public void saveAllArenas(List<Arena> arenas, MinecraftServer server) {
        for (Arena arena : arenas) {
            saveArena(arena, server);
        }
    }

    private static class Vec3dSerializer implements JsonSerializer<Vec3d> {
        @Override
        public JsonElement serialize(Vec3d src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("x", src.x);
            obj.addProperty("y", src.y);
            obj.addProperty("z", src.z);
            return obj;
        }
    }

    private static class Vec3dDeserializer implements JsonDeserializer<Vec3d> {
        @Override
        public Vec3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject obj = json.getAsJsonObject();
            double x = obj.get("x").getAsDouble();
            double y = obj.get("y").getAsDouble();
            double z = obj.get("z").getAsDouble();
            return new Vec3d(x, y, z);
        }
    }
}
